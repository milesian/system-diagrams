(ns milesian.system-diagrams
  (:require [camel-snake-kebab :refer (->kebab-case-keyword ->camelCaseString)]
            [cheshire.core :refer (encode decode)]
            [clojure.pprint :refer (pprint)]
            [clojure.string :as st]
            [clojure.walk :refer (postwalk)]
            [com.stuartsierra.component :as component]
            [org.httpkit.client :refer (request) :rename {request http-request}]))


(defn process-maps [fm t]
  (postwalk (fn [fm]
              (cond
               (map? fm) (reduce-kv (fn [acc k v] (assoc acc (t k) v)) {} fm)
               :otherwise fm)) fm))

(defn ->clj
  "Convert JSON keys into Clojure keywords. This is because we receive
  JSON but want to process it as Clojure."
  [fm]
  (process-maps fm ->kebab-case-keyword))

(defn ->js
  "Convert Clojure keywords into JSON keys. This is because we respond
  with JSON."
  [fm]
  (process-maps fm ->camelCaseString))

(defn request [method uri & {:keys [data]}]
  @(http-request
          (merge
           {:method method
            :url uri
            :headers
            (merge
             {"Content-Type" "application/json"
              "Accept" "application/json"})}

           (when data {:body (str (encode (->js data)))}))
          identity))

(defn replace- [s]
  (clojure.string/replace s #"-" "_"))

(def example-seq  "a->b:....\n c->d:....\n e->e:....")


(def store (atom []))

(defn store-message [m k]
  (swap! store conj [m k]))
(def port 8011)
(def url-seq  (format "http://localhost:%s/publish-sequence" port))
(def url-graph  (format "http://localhost:%s/publish-graph" port))


(defn protocol+fn [protocol fn-name]
  (str (last (st/split (str protocol) #"\.")) "/" fn-name ))

(defn protocol+fn+html [protocol fn-name]
  (str "<span style='background-color:pink;color:black;font-weight:bold;'>"(last (st/split (str protocol) #"\.")) "/" "</span>"
       "<span style='background-color:white;color:black;'>"
       fn-name "</span>"))

;;example sequence: "Alice->Bob: Hello Bob, how are you? \n Note right of Bob: Bob thinks \n Bob-->Alice: I am good thanks!"
(defn publish-sequence [store]
  (let [sequence (reduce (fn [s [{:keys [id who fn-name fn-args protocol]} k]]
                           (let [step-sequence (if (= :opened k)
                                           (format "%s->%s: %s" (replace- who) (replace- id) (protocol+fn protocol fn-name))
                                           (format "%s->%s:" (replace- id) (replace- who))
                                           )]
                             (str s step-sequence "\n"))) "" @store)]
    (request :post url-seq :data {:sequence sequence})))

(defn graph-dagree [store system]
  (let [highlight-nodes (reduce (fn [s [{:keys [id who fn-name fn-args]} k]]
                                  (conj s (replace- id) (replace- who))
                                  ) #{} @store)
        highlight-nodes-str (reduce #(str % (str %2 " [style=\"fill: #f77; font-weight: bold\"]; ")) "" highlight-nodes)

        opened-messages (reduce (fn [v [{:keys [id who fn-name fn-args protocol]} _]]
                                  (conj v [who id (protocol+fn+html protocol fn-name)])
                                  ) [] (filter #(= :opened (last %)) @store))


        opened-messages-deps (reduce
                              (fn [v [who id label]]
                                (let [dep (-> (map #(vector who (name (last %)) "")
                                                   (component/dependencies (get system (keyword who))))
                                                set
                                                )]
                                  (if (empty? dep)
                                    v
                                    (apply conj v dep))
                                  )
                                )

                              [] opened-messages)

        opened-messages-bis (reduce (fn [v [who id _]]
                                      (let [f (filter (fn [[who* id* _]]
                                                    (= (str who* id*) (str who id) )
                                                 )  opened-messages)]

                                    (if (empty? f)
                                      (conj v [who id ""])
                                      v
                                      ))
                                  ) [] opened-messages-deps)
        ;; _ (println "\nOPENED MESSAGES\n")
        ;; _ (clojure.pprint/pprint opened-messages)
        ;; _ (println "\nOPENED MESSAGES-DEPS\n")
        ;; _ (clojure.pprint/pprint opened-messages-deps)
        ;; _ (println "\nOPENED MESSAGES-BIS\n")
        ;; _ (clojure.pprint/pprint opened-messages-bis)
        opened-messages (apply conj opened-messages opened-messages-bis)

        ]
    (str "digraph {node [rx=5 ry=5 labelStyle=\"font: 300 14px Helvetica\"]; edge [labelStyle=\"font: 300 14px Helvetica\" ]; "
         highlight-nodes-str
         (reduce (fn [s [who id label]] (str s (replace- (name who)) " -> " (replace- (name id)) "[labelType=\"html\" label= \"" label "\"];"))
                 "" opened-messages)

         "}")
    ))


;;example graph format: "digraph {A -> B -> C; B -> D; D -> E;}"
;    node [rx=5 ry=5 labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"]
;    edge [labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"]
;; E [style="fill: #f77; font-weight: bold"];
(defn publish-graph [store system]
  (let [graph (graph-dagree store system)]
    (request :post url-graph :data {:graph graph})))

(defn try-to-publish [system]
  (let [closed (filter (fn[[_ k]] (= k :closed)) @store)]
    (when (= (count closed) (count (filter (fn[[_ k]] (= k :opened)) @store)))
      (let [m (reduce (fn [s [m _]] (str s m "\n")) "" @store)]
;        (clojure.pprint/pprint @s)
 ;       (println "\n-----\n")
        (publish-sequence store)
        (publish-graph store system)
        )
      (reset! store []))))

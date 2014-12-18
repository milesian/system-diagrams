(ns tangrammer.dashboard.sequence-diagram.webapp
  (:require
   [bidi.bidi :refer (make-handler ->ResourcesMaybe ->Files)]
   [ring.util.response :refer (response redirect file-response)]
   [modular.bidi :refer (WebService path-for)]
   [com.stuartsierra.component :as component :refer (using)]
   [org.httpkit.server :refer (send!)]
   [tangrammer.dashboard.sequence-diagram.utils :refer (->clj read-json-body)]
   [clostache.parser :refer (render-resource render)]
   [cheshire.core :refer (generate-string)]
   ))

(defn render-page
  ([data page]
     (render-resource
      (str (format "templates/%s.html.mustache" page))
      data)))


(def routes
  ["/" [["sequence" :sequence]
        ["graph" :graph]
        ["big-graph" :big-graph]
        ["publish-sequence" :publish-sequence]
        ["publish-graph" :publish-graph]
        ["" (->ResourcesMaybe {:prefix "public/"})]]])

(defn publish-message [ws m]
  (doseq [client @(:clients ws)]
      (send! (key client) (generate-string
                           {:sequence m})  false))
  (println "try to publish message!")
  )

(defn publish-graph [ws m]
  (doseq [client @(:clients ws)]
      (send! (key client) (generate-string
                           {:graph m
                            ;;"digraph {A -> B -> C; B -> D; D -> E; C -> E; A -> D; F -> J; E -> J;}"
                            })  false))
  (println "try to publish message!")
  )
(defrecord WebApp [ws port]
  WebService
  (request-handlers [this] {:publish-sequence (fn [req]
                                                (when-let [sequence-diagram  (:sequence (-> (:body req)  read-json-body ->clj))]
                                                  (println sequence-diagram)

                                                  (publish-message ws sequence-diagram)
                                                  (response  " >>>> sequence diagram published!")))
                            :publish-graph (fn [req]
                                             (when-let [graph-diagram  (:graph (-> (:body req)  read-json-body ->clj))]
                                               (println graph-diagram)

                                               (publish-graph ws graph-diagram)
                                               (response  " >>>> graph diagram published!")))

                            :sequence (fn [req] (response (render-page {:webapp-port port :port (:port ws)} "sequence")))
                            :graph (fn [req] (response (render-page {:webapp-port port :port (:port ws)} "graph")))
                            :big-graph (fn [req] (response (render-page {:webapp-port port :port (:port ws)} "big-graph")))
                            })
  (routes [_] routes)
  (uri-context [_] ""))


(defn new-webapp [& {:as opts}]
  (using (map->WebApp opts) [:ws]))

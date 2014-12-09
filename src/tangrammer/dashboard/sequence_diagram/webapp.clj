(ns tangrammer.dashboard.sequence-diagram.webapp
  (:require
   [bidi.bidi :refer (make-handler ->ResourcesMaybe ->Files)]
   [ring.util.response :refer (response redirect file-response)]
   [modular.bidi :refer (WebService path-for)]
   [com.stuartsierra.component :as component :refer (using)]
   [org.httpkit.server :refer (send!)]
   [tangrammer.dashboard.sequence-diagram.utils :refer (->clj read-json-body)]
   [clostache.parser :refer (render-resource render)]

   ))

(defn render-page
  ([data page]
     (render-resource
      (str (format "templates/%s.html.mustache" page))
      data)))


(def routes
  ["/" [["dashboard" :dashboard]
        ["dagree" :dagree]
        ["publish" :publish]
        ["" (->ResourcesMaybe {:prefix "public/"})]]])

(defn publish-message [ws m]
  (doseq [client @(:clients ws)]
      (send! (key client)  m false))
  (println "try to publish message!")
  )

(defrecord WebApp [ws port]
  WebService
  (request-handlers [this] {:publish (fn [req]
                                       (let [data  (:sequence (-> (:body req)  read-json-body ->clj))]
                                         (println data)
                                         (publish-message ws data)
                                         (response data))


                                       )
                            :dashboard (fn [req] (response (render-page {:webapp-port port :port (:port ws)} "index")))
                            :dagree (fn [req] (response (render-page {:webapp-port port :port (:port ws)} "system")))
                            })
  (routes [_] routes)
  (uri-context [_] ""))


(defn new-webapp [& {:as opts}]
  (using (map->WebApp opts) [:ws]))

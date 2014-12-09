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
  ([data]
     (render-resource
      (str "templates/index.html.mustache")
      data)))


(def routes
  ["/" [["dashboard" :dashboard]
        ["publish" :publish]
        ["" (->ResourcesMaybe {:prefix "public/"})]]])

(defn publish-message [ws m]
  (doseq [client @(:clients ws)]
      (send! (key client)  m false))
  (println "try to publish message!")
  )

(defrecord WebApp [ws]
  WebService
  (request-handlers [this] {:publish (fn [req]
                                       (let [data  (:sequence (-> (:body req)  read-json-body ->clj))]
                                         (println data)
                                         (publish-message ws data)
                                         (response data))


                                       )
                            :dashboard (fn [req] (response (render-page {:port (:port ws)})))
                            })
  (routes [_] routes)
  (uri-context [_] ""))


(defn new-webapp [& {:as opts}]
  (using (map->WebApp opts) [:ws]))

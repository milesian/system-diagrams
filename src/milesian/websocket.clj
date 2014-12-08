(ns milesian.websocket
  (:require [org.httpkit.server :refer (run-server with-channel on-close send!)]
            [compojure.core :refer (defroutes GET)]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :refer (redirect)]))

(def clients (atom {}))

(defn ws
  [req]
  (with-channel req con
    (swap! clients assoc con true)
    (println con " connected")
    (on-close con (fn [status]
                    (swap! clients dissoc con)
                    (println con " disconnected. status: " status)))))

(defn publish-message [m]
  (doseq [client @clients]
    (send! (key client)  m false)))

(defroutes routes
  (GET "/sequence_diagram" [] ws)
  (GET "/" [] (redirect "/index.html"))
  (route/resources "/")
  )


(def application  )

(defn start-server []
  (let [port (Integer/parseInt
              (or (System/getenv "PORT") "8088"))]
    (run-server (handler/site routes) {:port port :join? false})
    ))


(defn -main [& args]
  (start-server))

(defonce server (start-server))

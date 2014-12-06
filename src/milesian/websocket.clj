(ns milesian.websocket
  (:use [compojure.core :only (defroutes GET)]
        ring.util.response
        ring.middleware.cors
        org.httpkit.server)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :refer [redirect]]
            [ring.middleware.reload :as reload]
            [cheshire.core :refer :all]))

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
            (send! (key client) (generate-string {:diagram m}) false)))

(defroutes routes
  (GET "/sequence_diagram" [] ws)
  (GET "/" [] (redirect "/index.html"))
  (route/resources "/"))


(def application (-> (handler/site routes)
                     reload/wrap-reload
                     (wrap-cors
                      :access-control-allow-origin #".+")))

(defn start-server []
  (let [port (Integer/parseInt
               (or (System/getenv "PORT") "8088"))]
    (run-server application {:port port :join? false})))

(defn -main [& args]
  (start-server)
  )

(defonce server (start-server))

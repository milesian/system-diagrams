(ns tangrammer.dashboard.sequence-diagram.websocket
  (:require [com.stuartsierra.component :as component]
            [bidi.bidi :refer (make-handler)]


            [clojure.tools.logging :as log]
            [org.httpkit.server :refer [run-server with-channel send! on-close close]]))

(defn ws
  [req clients]
  (with-channel req con
    (swap! clients assoc con true)
    (println con " connected")
    (on-close con (fn [status]
                    (swap! clients dissoc con)
                    (println con " disconnected. status: " status)))))




(defrecord Websocket [port]
  component/Lifecycle
  (start [this]
    (let [clients (atom {})
          handler (make-handler ["/sequence_diagram" (fn [req] (ws req clients))])
          server (run-server handler {:port port})]
      (log/debugf "About to start WebSocket server on port %d" port)

      (assoc (assoc this :server server) :clients clients)
      ))
  (stop [this]
    (when-let [server (:server this)]
      (log/debugf "About to stop WebSocket server")
      (server))
    this))

(defn new-websocket
  [opts]
  (map->Websocket opts))

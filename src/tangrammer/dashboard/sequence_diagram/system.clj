(ns tangrammer.dashboard.sequence-diagram.system
  (:refer-clojure :exclude [read])
  (:require

   [com.stuartsierra.component :as component :refer (system-map system-using using)]

   [clojure.string :as str]
   [clojure.tools.logging :refer :all]

   ;; Pre-baked components
   [modular.http-kit :refer (new-webserver) :rename {new-webserver new-http-listener}]
   [modular.bidi :refer (new-router new-web-service)]


   [tangrammer.dashboard.sequence-diagram.websocket :refer (new-websocket)]

   [tangrammer.dashboard.sequence-diagram.webapp :refer (new-webapp)]

   [clojure.java.io :as io]
   [clojure.tools.reader :refer (read)]
   [clojure.tools.reader.reader-types :refer (indexing-push-back-reader)]))



(defn add-websocket [system config]
  (assoc system
    :ws-bridge (-> (new-websocket {:port (get-in config [:websocket :port])}))))

(defn add-webapp-server
  [system config]
  (assoc system
    :webapp
    (-> (new-webapp :port (get-in config [:webapp :port]))
        (using {:ws :ws-bridge}))

    :webapp-router
    (-> (new-router)
        (using [:webapp]))

    :webapp-listener
    (-> (new-http-listener :port (get-in config [:webapp :port]))
        (using {:request-handler :webapp-router}))))

(defn configurable-system-map
  "Build the system map, piece by piece"
  [config]
  (infof "Building system map")
  (apply system-map
         (apply concat
                (-> {}
                    (add-websocket config)
                    (add-webapp-server config)))))

(defn ^:private read-file
  [f]
  (read
   (indexing-push-back-reader
    (java.io.PushbackReader. (io/reader f)))))

(defn ^:private config-from
  [f]
  (if (.exists f)
    (read-file f)
    {}))

(defn config []
  (config-from (io/file "resources/.dashboard.edn"))
  )


(defn new-prod-system []
  (let [s-map (configurable-system-map (config))]
    (component/system-using s-map {})))

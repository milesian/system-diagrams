(ns milesian.system-diagrams.webclient.system
  (:refer-clojure :exclude [read])
  (:require

   [com.stuartsierra.component :as component :refer (system-map system-using using)]

   [clojure.string :as str]
   [clojure.tools.logging :refer :all]

   ;; Pre-baked components
   [modular.http-kit :refer (new-webserver) :rename {new-webserver new-http-listener}]
   [modular.bidi :refer (new-router new-web-service)]


   [milesian.system-diagrams.webclient.websocket :refer (new-websocket)]

   [milesian.system-diagrams.webclient.webapp :refer (new-webapp)]

   [clojure.java.io :as io]
   [clojure.tools.reader :refer (read)]
   [clojure.tools.reader.reader-types :refer (indexing-push-back-reader)]))



(defn add-websocket [system config]
  (assoc system
    ::ws-bridge (-> (new-websocket {:port (get-in config [:websocket :port])}))))

(defn add-webapp-server
  [system config]
  (assoc system
    ::webapp
    (-> (new-webapp :port (get-in config [:webapp :port]))
        (using {:ws ::ws-bridge}))

    ::webapp-router
    (-> (new-router)
        (using [::webapp]))

    ::webapp-listener
    (-> (new-http-listener :port (get-in config [:webapp :port]))
        (using {:request-handler ::webapp-router}))))

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
   ;; This indexing-push-back-reader gives better information if the
   ;; file is misconfigured.
   (indexing-push-back-reader
    (java.io.PushbackReader. (io/reader f)))))

(defn ^:private config-from
  [f]
  (if (.exists f)
    (read-file f)
    {}))

(defn ^:private user-config
  []
  (config-from (io/file (System/getProperty "user.home") ".dashboard.edn")))

(defn ^:private config-from-classpath
  []
  (if-let [res (io/resource "dashboard.edn")]
    (config-from (io/file res))
    {}))


(defn config
  "Return a map of the static configuration used in the component
  constructors."
  []
  (merge (config-from-classpath)
         (user-config)))

(defn new-prod-system []
  (let [s-map (configurable-system-map (config))]
    (component/system-using s-map {})))

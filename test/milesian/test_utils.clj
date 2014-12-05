(ns milesian.test-utils
  (:require [milesian.websocket :refer (start-server)]))

(defonce server (start-server))

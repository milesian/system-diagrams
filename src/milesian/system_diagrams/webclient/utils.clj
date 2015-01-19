(ns milesian.system-diagrams.webclient.utils
  (:require    [clojure.walk :refer (postwalk)]
               [clojure.string :as str]
               [cheshire.core :refer (decode decode-stream encode)]
               [clojure.edn :as edn]
               [clojure.java.io :as io]
               [camel-snake-kebab :refer (->kebab-case-keyword ->camelCaseString)]
))

(defprotocol Body
  (read-edn-body [body])
  (read-json-body [body]))

(extend-protocol Body
  String
  (read-edn-body [body] (edn/read-string body))
  (read-json-body [body] (decode body keyword))
  org.httpkit.BytesInputStream
  (read-edn-body [body] (io! (edn/read (java.io.PushbackReader. (io/reader body)))))
  (read-json-body [body] (io! (decode-stream (io/reader body) true))))

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

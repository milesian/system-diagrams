(ns milesian.system-diagrams.webclient.main
  "Main entry point"
  (:gen-class))

(defn -main [& args]
  ;; We eval so that we don't AOT anything beyond this class
  (eval '(do (require 'milesian.system-diagrams.webclient.system)
             (require 'com.stuartsierra.component)
             ;; TODO: Get from arguments
             (com.stuartsierra.component/start (milesian.system-diagrams.webclient.system/new-prod-system))
             (println "system-diagrams")
             (println "Ready..."))))

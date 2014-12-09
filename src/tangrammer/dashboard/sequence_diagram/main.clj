(ns tangrammer.dashboard.sequence-diagram.main
  "Main entry point"
  (:gen-class))

(defn -main [& args]
  ;; We eval so that we don't AOT anything beyond this class
  (eval '(do (require 'tangrammer.dashboard.sequence-diagram.system)
             (require 'com.stuartsierra.component)
             ;; TODO: Get from arguments
             (com.stuartsierra.component/start (tangrammer.dashboard.sequence-diagram/new-prod-system))
             (println "sequence-diagram dashboard")
             (println "Ready..."))))

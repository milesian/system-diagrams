(defproject tangrammer/webclient-system-diagram "0.1.4-SNAPSHOT"
  :description "sequence diagram html live renderer"
  :url "https://github.com/tangrammer/webclient-system-diagram"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.stuartsierra/component "0.2.2"]
                 [juxt.modular/http-kit "0.5.1"]
                 [juxt.modular/bidi "0.5.2"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [compojure "1.1.8"]
                 [cheshire                "5.3.1"]
                 [camel-snake-kebab "0.1.4"]
                 [org.clojure/tools.logging "0.2.6"]]
  :main tangrammer.dashboard.sequence-diagram.main
  :repl-options {:init-ns user}
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev/src/clojure" ]
                   :resource-paths ["test/resources"]}

             :uberjar {:main tangrammer.dashboard.sequence-diagram.main
                       :aot [tangrammer.dashboard.sequence-diagram.main]}})

(defproject milesian/system-diagrams "0.1.3-SNAPSHOT"
  :description "system diagrams formats and web client"
  :url "https://github.com/milesian/system-diagrams"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.stuartsierra/component "0.2.2"]
                 [juxt.modular/http-kit "0.5.3"]
                 [juxt.modular/bidi "0.6.1"]
                 [juxt.modular/clostache "0.6.0"]
                 [cheshire                "5.3.1"]
                 [camel-snake-kebab "0.1.4"]
                 [org.clojure/tools.logging "0.2.6"]]
  :main milesian.system-diagrams.webclient.main
  :repl-options {:init-ns user}
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.5"]]
                   :source-paths ["dev/src/clojure" ]
                   :resource-paths ["test/resources"]}

             :uberjar {:main milesian.system-diagrams.webclient.main
                       :aot [milesian.system-diagrams.webclient.main]}})

(defproject milesian/sequence-diagram-dashboard "0.1.0-SNAPSHOT"
  :description "milesian sequence diagram html live renderer"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cheshire "5.2.0"]
                 [ring/ring-devel "1.1.8"]
                 [http-kit "2.0.0"]
                 [compojure "1.1.5"]
                 [ring-cors "0.1.0"]]
  :profiles {:dev {:dependencies [[milesian/bigbang "0.1.1-SNAPSHOT"]
                                  [milesian/aop "0.1.2-SNAPSHOT"]
                                  [milesian/system-examples "0.1.1-SNAPSHOT"]
                                  [milesian/identity "0.1.2-SNAPSHOT"]]}})

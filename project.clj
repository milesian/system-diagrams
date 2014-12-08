(defproject milesian/sequence-diagram-dashboard "0.1.1-SNAPSHOT"
  :description "milesian sequence diagram html live renderer"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[juxt.modular/http-kit "0.5.1"]
                 [juxt.modular/bidi "0.5.2"]]

  :profiles {:dev {:dependencies [[milesian/bigbang "0.1.1-SNAPSHOT"]
                                  [milesian/aop "0.1.2-SNAPSHOT"]
                                  [milesian/system-examples "0.1.1-SNAPSHOT"]
                                  [milesian/identity "0.1.2-SNAPSHOT"]]}})

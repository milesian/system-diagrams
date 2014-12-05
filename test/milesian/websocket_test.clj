(ns milesian.websocket-test
  (:require [clojure.repl :refer (apropos dir doc find-doc pst source)]
            [clojure.test :refer :all]
            [milesian.test-utils :refer (server)]
            [milesian.websocket :refer (publish-message)]
            [com.stuartsierra.component :as component]
            [milesian.bigbang :as bigbang]
            [milesian.aop :as aop]
            [milesian.aop.matchers :as aop-matchers]
            [milesian.aop.utils  :refer (function-invocation)]
            [milesian.identity :as identity]
            [clojure.string :as st]
            [milesian.system-examples :refer (new-system-map listening talking Listen Talk)]))

(println "working?" server)

(def system-map (new-system-map))

(defn logging-function-invocation
  [*fn* this & args]
  (publish-message (function-invocation *fn* this args))
  (apply *fn* (conj args this)))


(def system (bigbang/expand system-map {:before-start [[identity/add-meta-key system-map]
                                                       [identity/assoc-meta-who-to-deps]]
                                        :after-start [[aop/wrap (aop-matchers/new-component-matcher :system system-map :components [:a :b :c] :fn logging-function-invocation)]

                                                      ]}))


;;(listening (:b system))
(talking (:c system))

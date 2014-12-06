(ns milesian.sequence-diagram
  (:require [milesian.websocket :refer (publish-message)]
            [milesian.aop.utils  :refer (function-invocation function-return)]))

(def store (atom []))

(defn store-message [m k]
  (swap! store conj [m k]))

(defn try-to-publish [s]
  (let [closed (filter (fn[[_ k]] (= k :closed)) @s)]
    (when (= (count closed) (count (filter (fn[[_ k]] (= k :opened)) @s)))
      (publish-message (reduce (fn [s [m _]] (str s m "\n")) "" @s))
      (reset! s []))))

(defn sequence-diagram-invocation
  [*fn* this & args]
  (store-message (function-invocation *fn* this args) :opened)
  (let [res (apply *fn* (conj args this))]
    (store-message (function-return *fn* this args) :closed)
    (try-to-publish store)
    res))

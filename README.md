# milesian/system-diagrams
This project is intended to get real time system visualisations, thus include:

 +  a standalone tool to visualise your system in real time.  Using clojure to get webserver entrypoints and websocket, and a couple of html pages to render data using js. 
 +  a system graph formatter fns utilities

So far it's ready for rendering **sequence call system diagrams** (using js lib: [bramp/js-sequence-diagrams](https://github.com/bramp/js-sequence-diagrams)) and **system graphs** (using js lib: [cpettitt/dagre](https://github.com/cpettitt/dagre) and [cpettitt/dagre-d3](https://github.com/cpettitt/dagre-d3)).

Besides these js resources it's written in clojure and mainly based on [http-kit](http://www.http-kit.org/),  [stuartsierra/component](https://github.com/stuartsierra/component), [juxt/modular](https://github.com/juxt/modular) and [milesian/aop](https://github.com/milesian/aop)

###Snapshots:-
The example project used to obtain these snapshots is build with [juxt/modular](https://github.com/juxt/modular) and uses [juxt/cylon](https://github.com/juxt/cylon) oauth2 feature
[<img src="https://user-images.githubusercontent.com/731829/36782346-f9a90b0a-1c78-11e8-8e62-24a91cc67463.png" alt="d'Alenerawing" style="width: 40%;"/>](https://user-images.githubusercontent.com/731829/36782346-f9a90b0a-1c78-11e8-8e62-24a91cc67463.png)
[<img src="https://user-images.githubusercontent.com/731829/36782351-fcaef904-1c78-11e8-95a5-37193c5d7888.png" alt="Drawing" style="width: 40%;"/>](https://user-images.githubusercontent.com/731829/36782351-fcaef904-1c78-11e8-95a5-37193c5d7888.png)


#### Releases and Dependency Information


```clojure
[milesian/system-diagrams "0.1.2"]
```

```clojure 
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.stuartsierra/component "0.2.2"]
                 [juxt.modular/http-kit "0.5.3"]
                 [juxt.modular/bidi "0.6.1"]
                 [juxt.modular/clostache "0.6.0"]
                 [cheshire                "5.3.1"]
                 [camel-snake-kebab "0.1.4"]
                 [org.clojure/tools.logging "0.2.6"]]
```




# Instructions to use it in your stuartsierra/component system to visualise your system calls

**Update your project deps**

```clojure
;; Update your dev dependencies 
:profiles {:dev {:dependencies [ ...
                                  [milesian/system-diagrams "0.1.3"]
                                  [milesian/bigbang "0.1.1"]
                                  [milesian/aop "0.1.5"]
                                  [milesian/identity "0.1.4"]
                                  ...
                                  ]
                                  ...
          }}
```

**Add dashboard.edn config to resources folder**

```clojure
{:webapp {:port 8011}
 :websocket {:port 8012}}
```





**Update your dev.clj**

```clojure

(ns dev
	(:require 
		 ...
   [milesian.bigbang :as bigbang]
   [milesian.identity :as identity]
   [milesian.aop :as aop]
   [milesian.aop.utils  :refer (extract-data)]
   [milesian.system-diagrams :refer (store-message try-to-publish store)]
   [milesian.system-diagrams.webclient.system :as wsd]

		 ...))

...

(defn new-dev-system
  "Create a development system"
  []
  (let [config (config)
        s-map (->
               (new-system-map config)
               (wsd/add-websocket (wsd/config))
               (wsd/add-webapp-server (wsd/config))

               #_(assoc
                     ))]
    (-> s-map
        (component/system-using (new-dependency-map))
        (co-dependency/system-co-using (new-co-dependency-map))
        )))

...

(defn diagram
  "to get sequence diagram we need the ->start-fn-call and
  the <-return-fn-call times of the fn invocation call.
  The sequence will be published if all fns are finished (:closed)"
  [*fn* this & args]
  (let [invocation-data (extract-data *fn* this args)]
    (store-message invocation-data :opened)
    (let [res (apply *fn* (conj args this))]
      (store-message invocation-data :closed)
      (try-to-publish system)
      res)))
      
; replace your current start fn by this one      
(defn start
  "Starts the current development system."
  []
  (alter-var-root #'system #(bigbang/expand % {:before-start [[identity/add-meta-key %]
                                                              [identity/assoc-meta-who-to-deps]]
                                               :after-start [[aop/wrap diagram]]}))) 
      
```



**Invoke your component-app and check your browsers:** [[sequence diagram]](http://localhost:8011/sequence) - [[graph diagram]](http://localhost:8011/graph)



### Instructions to use it as a standalone tool
[here](https://github.com/milesian/system-diagrams/wiki/Instructions-to-use-it-as-a-standalone-tool)


## License


Copyright © 2014 Juan Antonio Ruz

Distributed under the [MIT License](http://opensource.org/licenses/MIT). This means that pieces of this library may be copied into other libraries if they don't wish to have this as an explicit dependency, as long as it is credited within the code.

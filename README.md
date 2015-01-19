# milesian/system-diagrams
This project is intended to get real time system visualisations, thus include:

 +  a standalone tool to visualise your system in real time. Thus it includes a couple of server entrypoints (to recieve the data to represent), a websocket and a couple of html pages to render data using js. 
 +  a system graph formatter fns utilities

So far it's ready for rendering **sequence call system diagrams** (using js lib: [bramp/js-sequence-diagrams](https://github.com/bramp/js-sequence-diagrams)) and **system graphs** (using js lib: [cpettitt/dagre](https://github.com/cpettitt/dagre) and [cpettitt/dagre-d3](https://github.com/cpettitt/dagre-d3)).

Besides these js resources it's written in clojure and mainly based on [http-kit](http://www.http-kit.org/),  [stuartsierra/component](https://github.com/stuartsierra/component), [juxt/modular](https://github.com/juxt/modular) and [milesian/aop](https://github.com/milesian/aop)

###Snapshots :-
[<img src="https://dl.dropboxusercontent.com/u/8688858/real-system-visualisations/graph.png" alt="d'Alenerawing" style="width: 40%;"/>](https://dl.dropboxusercontent.com/u/8688858/real-system-visualisations/graph.png)
[<img src="https://dl.dropboxusercontent.com/u/8688858/real-system-visualisations/sequence.png" alt="Drawing" style="width: 40%;"/>](https://dl.dropboxusercontent.com/u/8688858/real-system-visualisations/sequence.png)


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


# Instructions to use it as a standalone tool

#### Get the project 

```
$ git clone git@github.com:milesian/system-diagrams.git
$ cd system-diagrams
```

#### Start server side 

By default dashboard will use 8011 and 8012 ports (you can change the configuration in [dashboard.edn](https://github.com/milesian/system-diagrams/blob/master/resources/.dashboard.edn))

Start the dashboard and open the web client

`$ lein run`

#### Open web clients (browser tabs) 

[http://localhost:8011/sequence](http://localhost:8011/sequence)   
[http://localhost:8011/graph](http://localhost:8011/graph)

#### Sending system sequences data

Open your [web client](http://localhost:8011/sequence)   

You need to send your data sequence to this entrypoint ```http://localhost:8011/publish-sequence``` following [bramp/js-sequence-diagrams](https://github.com/bramp/js-sequence-diagrams) data format.

Example using curl:
```
curl -H "Content-Type: application/json" -d '{"sequence":"Alice->Bob: Hello Bob, how are you? \n Note right of Bob: Bob thinks \n Bob-->Alice: I am good thanks!"}' http://localhost:8011/publish-sequence
```


#### Sending system graphs data
Open your [web client](http://localhost:8011/graph)   

You need to send your data graph to this entrypoint ```http://localhost:8011/publish-graph``` following [cpettitt/dagre-d3](https://github.com/cpettitt/dagre-d3) data format.

Example using curl:
```
curl -H "Content-Type: application/json" -d '{"graph":"digraph {A -> B -> C; B -> D; D -> E;}"}' http://localhost:8011/publish-graph
```


# Instructions to use it in your stuartsierra/component system to visualise your system calls

**Update your project deps**

```clojure
;; Update your dev dependencies 
:profiles {:dev {:dependencies [ ...
                                  [milesian/system-diagrams "0.1.2"]
                                  [milesian/bigbang "0.1.1"]
                                  [milesian/aop "0.1.4"]
                                  [milesian/identity "0.1.3"]
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
   [milesian.sequence-diagram :refer (store-message try-to-publish store)]
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




## License


Copyright © 2014 Juan Antonio Ruz

Distributed under the [MIT License](http://opensource.org/licenses/MIT). This means that pieces of this library may be copied into other libraries if they don't wish to have this as an explicit dependency, as long as it is credited within the code.

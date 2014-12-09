# webclient-system-diagram

With this webclient (althought it's incluedes also the server side) You can visualise sequence call system diagrams (using js lib: [bramp/js-sequence-diagrams](https://github.com/bramp/js-sequence-diagrams)) and system graphs (using js lib: [cpettitt/dagre](https://github.com/cpettitt/dagre) and [cpettitt/dagre-d3](https://github.com/cpettitt/dagre-d3)).


It's based on webapp and websocket. It uses [stuartsierra/component](https://github.com/stuartsierra/component) and [juxt/modular](https://github.com/juxt/modular)

It is intended to use as standalone app

###Snapshot of a sequence of sequence diagrams :-
![image](https://dl.dropboxusercontent.com/u/8688858/seq.png)

###Snapshot of a sequence of system graphs
![image](https://dl.dropboxusercontent.com/u/8688858/seq.png)


# Simple Usage

### Start server side and open clients

By default dashboard will use 8011 and 8012 ports (you can change the configuration in [.dashboard.edn](https://github.com/tangrammer/sequence-diagram-dashboard/blob/master/resources/.dashboard.edn))

Start the dashboard and open the web client

```clojure
$ cd milesian-sequence-diagram
$ lein repl
$ (dev)
$ (go)
$ (clojure.java.browse/browse-url "http://localhost:8011/sequence")
$ (clojure.java.browse/browse-url "http://localhost:8011/graph")

```

### From anywhere (any app you need to visualize) make post calls to send values to visualize

```

curl -H "Content-Type: application/json" -d '{"sequence":"Alice->Bob: Hello Bob, how are you? \n Note right of Bob: Bob thinks \n Bob-->Alice: I am good thanks!"}' http://localhost:8011/publish-sequence

curl -H "Content-Type: application/json" -d '{"graph":"digraph {A -> B -> C; B -> D; D -> E; ;}"}' http://localhost:8011/publish-graph

```


## License


Copyright © 2014 Juan Antonio Ruz

Distributed under the [MIT License](http://opensource.org/licenses/MIT). This means that pieces of this library may be copied into other libraries if they don't wish to have this as an explicit dependency, as long as it is credited within the code.

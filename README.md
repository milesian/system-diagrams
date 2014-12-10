# webclient-system-diagram 
### server side included too!
Visualise your system in real time using this standalone tool.

So far it's ready for rendering **sequence call system diagrams** (using js lib: [bramp/js-sequence-diagrams](https://github.com/bramp/js-sequence-diagrams)) and **system graphs** (using js lib: [cpettitt/dagre](https://github.com/cpettitt/dagre) and [cpettitt/dagre-d3](https://github.com/cpettitt/dagre-d3)).

Besides these js resources it's written in clojure and mainly based on [http-kit](http://www.http-kit.org/),  [stuartsierra/component](https://github.com/stuartsierra/component) and [juxt/modular](https://github.com/juxt/modular)

###Snapshot :-
![image](https://dl.dropboxusercontent.com/u/8688858/graph_seq.png)


# Simple Usage

## Get the project (using git or lein-try)

Using git 

```
$ git clone git@github.com:tangrammer/webclient-system-diagram.git
$ cd webclient-system-diagram
$ lein repl

```

Using [lein-try](https://github.com/rkneufeld/lein-try)
```
$ lein try tangrammer/webclient-system-diagram "0.1.5"

```

### Start server side 

By default dashboard will use 8011 and 8012 ports (you can change the configuration in [.dashboard.edn](https://github.com/tangrammer/sequence-diagram-dashboard/blob/master/resources/.dashboard.edn))

Start the dashboard and open the web client

```clojure
user=> (dev)
dev=> (go)

```

### Open web clients (browser tabs) 

http://localhost:8011/sequence   
http://localhost:8011/graph

## visualising sequences
You need to send your data sequence to this entrypoint ```http://localhost:8011/publish-sequence``` following [bramp/js-sequence-diagrams](https://github.com/bramp/js-sequence-diagrams) data format.

Example using curl:
```
curl -H "Content-Type: application/json" -d '{"sequence":"Alice->Bob: Hello Bob, how are you? \n Note right of Bob: Bob thinks \n Bob-->Alice: I am good thanks!"}' http://localhost:8011/publish-sequence
```

## visualising graphs
You need to send your data graph to this entrypoint ```http://localhost:8011/publish-graph``` following [cpettitt/dagre-d3](https://github.com/cpettitt/dagre-d3) data format.

Example using curl:
```
curl -H "Content-Type: application/json" -d '{"graph":"digraph {A -> B -> C; B -> D; D -> E;}"}' http://localhost:8011/publish-graph
```


## License


Copyright © 2014 Juan Antonio Ruz

Distributed under the [MIT License](http://opensource.org/licenses/MIT). This means that pieces of this library may be copied into other libraries if they don't wish to have this as an explicit dependency, as long as it is credited within the code.

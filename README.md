# sequence-diagram-dashboard

This is a clojure tool for displaying HTML sequence diagrams. It's based on webapp and websocket. It uses [stuartsierra/component](https://github.com/stuartsierra/component) and [juxt/modular](https://github.com/juxt/modular)

This project uses [bramp/js-sequence-diagrams](https://github.com/bramp/js-sequence-diagrams) for js/HTML SVG rendering


![image](https://dl.dropboxusercontent.com/u/8688858/seq.png)


## Simple Usage following REPL

By default dashboard will use 8011 and 8012 ports (you can change the configuration in [.dashboard.edn](https://github.com/tangrammer/sequence-diagram-dashboard/blob/master/resources/.dashboard.edn))

Start the dashboard and open the web client

```clojure
$ cd milesian-sequence-diagram
$ lein repl
$ (dev)
$ (go)
$ (clojure.java.browse/browse-url "http://localhost:8011/dashboard")

```

From anywhere (any app you need to visualize) make post calls to sending sequence value to visualize

``` 

curl -H "Content-Type: application/json" -d '{"sequence":"Alice->Bob: Hello Bob, how are you? \n Note right of Bob: Bob thinks \n Bob-->Alice: I am good thanks!"}' http://localhost:8011/publish
```


## License


Copyright © 2014 Juan Antonio Ruz 

Distributed under the [MIT License](http://opensource.org/licenses/MIT). This means that pieces of this library may be copied into other libraries if they don't wish to have this as an explicit dependency, as long as it is credited within the code.

# milesian/sequence-diagram-dashboard

This is a tool for displaying HTML sequence diagrams.

Using milesian/aop we can log the component call sequence, so it's very easy to render as sequence diagrams.

This project uses [bramp/js-sequence-diagrams](https://github.com/bramp/js-sequence-diagrams) for js/HTML SVG rendering


![image](https://dl.dropboxusercontent.com/u/8688858/milesian-sequence-diagram.png)


## Simple Usage following REPL

```clojure
user> (use 'milesian.websocket-test)
user> (in-ns 'milesian.websocket-test)
... open your browser (by default port 8088 defined in websocket.clj )
milesian.websocket-test> (clojure.java.browse/browse-url "http://localhost:8088/")
 ... invoke your components
milesian.websocket-test> (listening (:b system))
 ... check your browser
 ... invoke your components again
milesian.websocket-test> (talking (:c system))
 ... check your browser again

```


## License

The way that server and client communicates with websocket is taken from [samrat/happy-dashboard](https://github.com/samrat/happy-dashboard)

The rest of the code/idea ...

Copyright © 2014 Juan Antonio Ruz (juxt.pro)

Distributed under the [MIT License](http://opensource.org/licenses/MIT). This means that pieces of this library may be copied into other libraries if they don't wish to have this as an explicit dependency, as long as it is credited within the code.

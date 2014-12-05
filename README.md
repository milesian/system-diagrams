# milesian/sequence-diagram-dashboard

This is a tool for displaying HTML sequence diagrams.

Using milesian/aop we can log the component call sequence, so it's very easy to render as sequence diagrams.

This project uses [bramp/js-sequence-diagrams](https://github.com/bramp/js-sequence-diagrams) for js/HTML SVG rendering


![image](https://dl.dropboxusercontent.com/u/8688858/milesian-sequence-diagram.png)
 
 
## Usage
1. evaluate test/milesian/test_utils.clj
2. [Open](http://localhost:8088/index.html) http://localhost:8088/index.html   
3. evaluate test/milesian/websocket_test.clj



## License

The way that server and client communicates with websocket is taken from [samrat/happy-dashboard](https://github.com/samrat/happy-dashboard)

The rest of the code/idea ...  

Copyright © 2014 Juan Antonio Ruz (juxt.pro)

Distributed under the [MIT License](http://opensource.org/licenses/MIT). This means that pieces of this library may be copied into other libraries if they don't wish to have this as an explicit dependency, as long as it is credited within the code.


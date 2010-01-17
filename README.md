# Clojure client for RabbitMQ (AMQP) #


## Installation ##
   get lein from http://github.com/technomancy/leiningen

   lein deps
   

## Usage and API ##
  
   add below to the dependencies list in your project.clj

   [clojure-rabbitmq "0.2.1"]


See `example/publisher.clj` and `example/consumer.clj` for usage.

To test connection to the rabbitmq server, run:
  example/test-connection.clj

To test publishing of messages, run:
  example/publisher.clj


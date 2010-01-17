# Clojure client for RabbitMQ (AMQP) #


## Installation ##
   get lein from http://github.com/technomancy/leiningen

   lein deps
   
## API ##

Add [clojure-rabbitmq "0.2.1"] to the dependencies list in your project.clj
     
    (ns rabbitmq-publisher-test
       (:require [org.clojars.rabbitmq :as rabbitmq]))

    (defonce conn-map {:username "guest"
                       :password "guest"
                       :host "localhost"
                       :port 5672
                       :virtual-host "/"
                       :type "direct"
                       :exchange "sorting-room"
                       :queue "po-box"
                       :durable true
                       :routing-key "<somekey>"})
    (defonce connection (connect conn-map))
    (let [[conn channel] connection]
      (do
        (rabbitmq/bind-channel conn-map channel)
        (println "rabbitmq publishing:" (format "message%d" @c))
        (rabbitmq/publish conn-map channel (format "message%d" @c))))
    

## Examples ##
  
See `example/publisher.clj` and `example/consumer.clj` for usage.

To test connection to the rabbitmq server, run:
  example/test-connection.clj

To test publishing of messages, run:
  example/publisher.clj

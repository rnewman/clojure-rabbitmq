# Clojure client for RabbitMQ (AMQP) #


## INSTALLATION ##
   lein deps && lein jar

   If you are using it as a library within your project using lein:
     add [clojure-rabbitmq "0.2.1"] to the dependencies list in your
     project.clj

## EXAMPLE USAGE ##

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
    (def c (ref 0))
  
    (defonce connection (connect conn-map))

    (let [[conn channel] connection]
      (do
        (rabbitmq/bind-channel conn-map channel)
        (println "rabbitmq publishing:" (format "message%d" @c))
        (rabbitmq/publish conn-map channel (format "message%d" @c))))
    
Also See `example/publisher.clj` and `example/consumer.clj` for usage.

To test connection to the rabbitmq server, run:
  example/test-connection.clj

To test publishing of messages, run:
  example/publisher.clj

## APIs ##
    (rabbitmq/connect connection-map) -> [connection channel]
    (rabbitmq/bind-channel connection-map channel) -> bool
    (rabbitmq/publish connection-map channel message) -> bool

    (rabbitmq/consume-wait connection-map channel) -> message
    (rabbitmq/consume-poll connection-map channel) -> message

    (rabbitmq/disconnect connection channel)

    Note: The consumer part of the client and other utils are still under development

   
       
    

     

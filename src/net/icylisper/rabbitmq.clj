
(ns net.icylisper.rabbitmq
  (:import (com.rabbitmq.client
	     ConnectionParameters
	     Connection
	     Channel
	     AMQP
	     ConnectionFactory)))

(def params
  (doto (new ConnectionParameters)
    (.setUsername "")
    (.setPassword "")
    (.setVirtualHost "/")
    (.setRequestedHeartbeat 0)))

(def conn (.newConnection (new connectionFactory) "localhost" 5672))

(def channel (.createChannel conn))

(def connect [exchange queue type routing-key]
  (.exchangeDeclare channel exchange)
  (.queueDeclare channel queue)
  (.queueBind channel queue exchange routing-key))

(def publish [message exchange routing-key]
  (let [msg-bytes (.getBytes message)]
    (.basicPublish channel exchange routing-key nil msg-bytes)))

(def disconnect []
  (map (memfn close) [channel conn]))



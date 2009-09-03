
(ns com.github.icylisper.rabbitmq
  (:import (com.rabbitmq.client
	     ConnectionParameters
	     Connection
	     Channel
	     AMQP
	     ConnectionFactory)))

(defn connect [conn-map]
  (let [params (doto (new ConnectionParameters)
		(.setUsername (:username conn-map))
		(.setPassword (:password conn-map))
		(.setVirtualHost (:virtual-host conn-map))
		(.setRequestedHeartbeat 0))
       conn (.newConnection (new ConnectionFactory params)
	       (:host conn-map) (:port conn-map))
       channel (.createChannel conn)]
    channel))

(defn bind-queue [conn-map channel]
  (.exchangeDeclare channel (:exchange conn-map) (:type conn-map))
  (.queueDeclare channel (:queue conn-map))
  (.queueBind channel (:queue conn-map) (:exchange conn-map) (:routing-key conn-map)))

(defn publish [conn-map channel message]
  (let [msg-bytes (.getBytes message)]
    (.basicPublish channel (:exchange conn-map) (:routing-key conn-map) nil msg-bytes)))

(defn disconnect [channel conn]
  (map (memfn close) [channel conn]))

(comment
  ; usage
  (let [conn-map { :username "guest"
		   :password "guest"
		   :host "localhost"
		   :port 5672
		   :virtual-host "/"
		   :type "direct"
		   :exchange "sorting-room"
		   :queue "po-box"
		   :routing-key "tata"}
	    channel (connect conn-map)]
    (bind-queue conn-map channel)
    (publish conn-map channel "message")))



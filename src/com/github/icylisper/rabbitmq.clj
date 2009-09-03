
(ns com.github.icylisper.rabbitmq
  (:gen-class)
  (:import (com.rabbitmq.client
	     ConnectionParameters
	     Connection
	     Channel
	     AMQP
	     ConnectionFactory
	     QueueingConsumer)))


(defn connected? [conn-map]
  true)

(defmacro with-channel
  [[var connection] & body]
  `(with-open [~var (.createChannel connection)]
     ~@body))

(defmacro with-connection
  [] []
  )

(defn connect [conn-map]
  (let [params (doto (new ConnectionParameters)
		(.setUsername (:username conn-map))
		(.setPassword (:password conn-map))
		(.setVirtualHost (:virtual-host conn-map))
		(.setRequestedHeartbeat 0))
       conn (.newConnection (new ConnectionFactory params)
	       (:host conn-map) (:port conn-map))
       channel (.createChannel conn)]
    [conn channel]))

(defn channel-bind [conn-map channel]
  (.exchangeDeclare channel (:exchange conn-map) (:type conn-map))
  (.queueDeclare channel (:queue conn-map))
  (.queueBind channel (:queue conn-map) (:exchange conn-map) (:routing-key conn-map)))

(defn publish [conn-map channel message]
  (let [msg-bytes (.getBytes message)]
    (.basicPublish channel (:exchange conn-map) (:routing-key conn-map) nil msg-bytes)))

(defn disconnect [channel conn]
  (map (memfn close) [channel conn]))


;;;; AMPQ Queue as a sequence
(defn delivery-seq [ch q]
  (lazy-seq
    (let [d (.nextDelivery q)
          m (String. (.getBody d))]
      (.basicAck ch (.. d getEnvelope getDeliveryTag) false)
      (cons m (delivery-seq ch q)))))

(defn queue-seq [conn queue-name]
  "Reutrn a sequence of the messages in queue with name queue-name"
  (let [ch (.createChannel conn)]
    (.queueDeclare ch queue-name)
    (let [consumer (QueueingConsumer. ch)]
      (.basicConsume ch queue-name consumer)
      (delivery-seq ch consumer))))


(comment
  ; usage

  (defonce conn-map { :username "guest"
		   :password "guest"
		   :host "localhost"
		   :port 5672
		   :virtual-host "/"
		   :type "direct"
		   :exchange "sorting-room"
		   :queue "po-box"
		   :routing-key "tata"})
  (defonce connection (connect conn-map))
  ;; TODO
  ;; (m-v-b [conn channel] (connect conn-map))

  
  (let [[conn channel] connection]
    (channel-bind conn-map channel)
    (publish conn-map channel "message"))

  ;; FIXME : replace with with-connection
  
  :or 
  (let [conn-map { :username "guest"
		   :password "guest"
		   :host "localhost"
		   :port 5672
		   :virtual-host "/"
		   :type "direct"
		   :exchange "sorting-room"
		   :queue "po-box"
		   :routing-key "tata"}
	[conn channel] (connect conn-map)]
    (channel-bind conn-map channel)
    (publish conn-map channel "message")))





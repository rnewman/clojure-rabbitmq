
(ns com.github.icylisper.rabbitmq
  (:gen-class)
  (:import (com.rabbitmq.client
	     ConnectionParameters
	     Connection
	     Channel
	     AMQP
	     ConnectionFactory
	     Consumer
	     QueueingConsumer)))


(defn connected? [conn-map]
  true)

(defmacro with-channel
  [[var connection] & body]
  `(with-open [~var (.createChannel connection)]
     ~@body))


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

(defn bind-channel [conn-map channel]
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
  "Return a sequence of the messages in queue with name queue-name"
  (let [ch (.createChannel conn)]
    (.queueDeclare ch queue-name)
    (let [consumer (QueueingConsumer. ch)]
      (.basicConsume ch queue-name consumer)
      (delivery-seq ch consumer))))

(defn consume-wait [conn-map channel]
  (let [consumer (QueueingConsumer. channel)]
    (.queueDeclare channel (:queue conn-map))
    (.basicConsume channel (:queue conn-map) consumer)
    (let [d (.nextDelivery consumer)
          m (String. (.getBody d))]
      (.basicAck channel (.. d getEnvelope getDeliveryTag) false)
      m)))




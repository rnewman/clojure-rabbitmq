
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


;; abbreviatons:
;; ch - channel
;; c  - connection-map
;; q  - queue
;; m  - message
;; d  - delivery

(defn connected? [cm]
  true)

(defn connect [c]
  (let [params (doto (new ConnectionParameters)
		(.setUsername (:username c))
		(.setPassword (:password c))
		(.setVirtualHost (:virtual-host c))
		(.setRequestedHeartbeat 0))
       conn (.newConnection (new ConnectionFactory params)
	       (:host c) (:port c))
       ch (.createChannel conn)]
    [conn ch]))

(defn bind-channel [c ch]
  (.exchangeDeclare ch (:exchange c) (:type c))
  (.queueDeclare ch (:queue c))
  (.queueBind ch (:queue c) (:exchange c) (:routing-key c)))

(defn publish [c ch m]
  (let [msg-bytes (.getBytes m)]
    (.basicPublish ch (:exchange c) (:routing-key c) nil msg-bytes)))

(defn disconnect [ch conn]
  (map (memfn close) [ch conn]))

;;;; AMPQ Queue as a sequence
(defn delivery-seq [ch q]
  (lazy-seq
    (let [d (.nextDelivery q)
          m (String. (.getBody d))]
      (.basicAck ch (.. d getEnvelope getDeliveryTag) false)
      (cons m (delivery-seq ch q)))))

(defn queue-seq [conn ch {q :queue}]
  "Return a sequence of the messages in queue with name queue-name"
  (.queueDeclare ch q)
  (let [consumer (QueueingConsumer. ch)]
      (.basicConsume ch q consumer)
      (delivery-seq ch consumer)))

;;; consumer routines
(defn consume-wait [c ch]
  (let [consumer (QueueingConsumer. ch)]
    (.queueDeclare ch (:queue c))
    (.basicConsume ch (:queue c) false consumer)
    (while true
      (let [d (.nextDelivery consumer)
          m (String. (.getBody d))]
        (.basicAck ch (.. d getEnvelope getDeliveryTag) false)
	m))))

(defn consume-poll [c ch]
  (let [consumer (QueueingConsumer. ch)]
    (.queueDeclare ch (:queue c))
    (.basicConsume ch (:queue c) false consumer)
    (let [d (.nextDelivery consumer)
          m (String. (.getBody d))]
      m)))


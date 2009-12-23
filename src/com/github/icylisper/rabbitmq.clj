(set! *warn-on-reflection* true)

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

(defn connect [{:keys [username password virtual-host port
                       #^String host]}]
  (let [#^ConnectionParameters params
        (doto (new ConnectionParameters)
          (.setUsername username)
          (.setPassword password)
          (.setVirtualHost virtual-host)
          (.setRequestedHeartbeat 0))
        
        #^Connection conn
        (let [#^ConnectionFactory f
              (new ConnectionFactory params)]
          (.newConnection f host (int port)))]
    
    [conn (.createChannel conn)]))

(defn bind-channel [{:keys [exchange type queue routing-key durable]}
                    #^Channel ch]
  (.exchangeDeclare ch exchange type durable)
  (.queueDeclare ch queue durable)
  (.queueBind ch queue exchange routing-key))

(defn publish [{:keys [exchange routing-key]}
               #^Channel ch
               #^String m]
  (let [msg-bytes (.getBytes m)]
    (.basicPublish ch exchange routing-key nil msg-bytes)))

(defn disconnect [#^Channel ch
                  #^Connection conn]
  (.close ch)
  (.close conn))

;;;; AMPQ Queue as a sequence
(defn delivery-seq [#^Channel ch
                    #^QueueingConsumer q]
  (lazy-seq
    (let [d (.nextDelivery q)
          m (String. (.getBody d))]
      (.basicAck ch (.. d getEnvelope getDeliveryTag) false)
      (cons m (delivery-seq ch q)))))

(defn queue-seq
  "Return a sequence of the messages in queue with name queue-name"
  ([#^Channel ch
    {q :queue}]
   (.queueDeclare ch q)
   (let [consumer (QueueingConsumer. ch)]
     (.basicConsume ch q consumer)
     (delivery-seq ch consumer)))
  ([conn
    #^Channel ch
    c]
   (queue-seq ch c)))
  

;;; consumer routines
(defn consume-wait [c #^Channel ch]
  (let [consumer (QueueingConsumer. ch)]
    (.queueDeclare ch (:queue c))
    (.basicConsume ch (:queue c) false consumer)
    (while true
      (let [d (.nextDelivery consumer)
          m (String. (.getBody d))]
        (.basicAck ch (.. d getEnvelope getDeliveryTag) false)
        m))))

(defn consume-poll [c #^Channel ch]
  (let [consumer (QueueingConsumer. ch)]
    (.queueDeclare ch (:queue c))
    (.basicConsume ch (:queue c) false consumer)
    (let [d (.nextDelivery consumer)
          m (String. (.getBody d))]
      m)))

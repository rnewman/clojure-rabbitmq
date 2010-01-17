(ns rabbitmq.consumer.test
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
                   :routing-key "tata"})

(defonce connection (rabbitmq/connect conn-map))

(def c (ref 0))

(while true
  (dosync (alter c inc))
  (println "cycle: " @c)

  ;; publish
  (let [[_ channel] connection
        message     (rabbitmq/consume-poll conn-map channel)]
    (println "rabbitmq consumer : got message" message))
  
  (Thread/sleep 1000))


;; (let [[_ channel] connection]
;;   (println (rabbitmq/consume-poll conn-map channel)))

;; (let [[conn channel] connection]
;;   (println (rabbitmq/queue-seq channel conn-map)))


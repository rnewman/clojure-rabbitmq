(ns rabbitmq.consumer.test
  (:require [com.github.icylisper.rabbitmq :as rabbitmq]))

(defonce conn-map { :username "guest"
		    :password "guest"
		    :host "localhost"
		    :port 5672
		    :virtual-host "/"
		    :type "direct"
		    :exchange "sorting-room"
		    :queue "po-box"
		    :routing-key "tata"})

(defonce connection (rabbitmq/connect conn-map))

(def c (ref 0))

(while true
  (dosync (alter c inc))
  (println "cycle: " @c)

  ;; publish
  (let [[_ channel] connection
	message        (rabbitmq/consume-wait conn-map channel)]
    (println "rabbitmq consumer : got message" message))
  
  (Thread/sleep 1000))



(ns rabbitmq.publisher
  (:require [com.github.icylisper.rabbitmq :as rabbitmq]))

(defonce conn-map {:username "guest"
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
  (let [[conn channel] connection]
    (do
      (rabbitmq/bind-channel conn-map channel)
      (println "rabbitmq publishing:" (format "message%d" @c))
      (rabbitmq/publish conn-map channel (format "message%d" @c))))
  
  (Thread/sleep 1000))

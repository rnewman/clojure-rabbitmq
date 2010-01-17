#^:shebang '[
exec java -cp "lib/*:$PWD/*" clojure.main "$0" -- "$@"
]

(ns rabbitmq-publisher
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

(println conn-map)

(defonce connection (rabbitmq/connect conn-map))

(println connection)

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

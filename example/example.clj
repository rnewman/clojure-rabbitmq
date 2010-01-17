(use 'org.clojars.rabbitmq)

;; example(1)
(defonce conn-map {:username "guest"
                   :password "guest"
                   :host "localhost"
                   :port 5672
                   :virtual-host "/"
                   :type "direct"
                   :exchange "sorting-room"
                   :queue "po-box"
                   :routing-key "tata"})

(defonce connection (connect conn-map))

(println connection)

;; (let [[conn channel] connection]
;;   (bind-channel conn-map channel)
;;   (publish conn-map channel "message"))


;; example(2)
(let [conn-map {:username "guest"
                :password "guest"
                :host "localhost"
                :port 5672
                :virtual-host "/"
                :type "direct"
                :exchange "sorting-room"
                :queue "po-box"
                :routing-key "tata"}
      [conn channel] (connect conn-map)]
  (bind-channel conn-map channel)
  (publish conn-map channel "message"))

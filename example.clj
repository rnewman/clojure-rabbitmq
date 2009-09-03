
(use 'com.github.icylisper.rabbitmq)

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
  (bind-queue conn-map channel)
  (publish conn-map channel "message"))


;; OR

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
  (publish conn-map channel "message"))
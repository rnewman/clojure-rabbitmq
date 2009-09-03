
(use 'com.github.icylisper.rabbitmq)

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
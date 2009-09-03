
Clojure client for rabbitmq AMQP

* INSTALLATION

  Get the latest java rabbitmq-client from http://www.rabbitmq.com/java-client.html


* USAGE  

   <pre>
   <code>
   (use 'com.github.icylisper.rabbitmq)
   </code>
   </pre>

   <pre>
   <code>
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
   </code>   
   </pre>
	   
# Clojure client for RabbitMQ (AMQP) #

## Installation ##

Get the latest java rabbitmq-client from <http://www.rabbitmq.com/java-client.html>.

You can use the `fetchdeps.sh` script to do this; the RabbitMQ 1.7.0 Java
client will be fetched and unpacked into `lib/`, where `build.xml` expects it.

Build `clojure-rabbitmq.jar` by invoking `ant` with the appropriate
arguments, e.g.:

    ant -Dclojure.jar=/opt/clojure/clojure.jar -Dcontrib.jar=/opt/clojure-contrib/clojure-contrib.jar


## Usage and API ##

Ensure that `clojure-rabbitmq.jar` and the RabbitMQ jars are in your classpath.

`.clojure` is set up appropriately for the common `clj` script.

Then:

    (use 'com.github.icylisper.rabbitmq)


See `example/publisher.clj` and `example/consumer.clj` for usage.

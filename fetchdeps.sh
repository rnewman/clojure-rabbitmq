mkdir -p lib
cd lib
wget http://www.rabbitmq.com/releases/rabbitmq-java-client/v1.7.0/rabbitmq-java-client-bin-1.7.0.tar.gz
tar xzf rabbitmq-java-client-bin-1.7.0.tar.gz
mv rabbitmq-java-client-bin-1.7.0/commons-cli-1.1.jar .
mv rabbitmq-java-client-bin-1.7.0/commons-io-1.2.jar .
mv rabbitmq-java-client-bin-1.7.0/rabbitmq-client.jar .
rm -r rabbitmq-java-client-bin-1.7.0*

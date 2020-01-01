#!/bin/sh

echo "********************************************************"
echo "Waiting for the Eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z eurekasvr $EUREKASERVER_PORT`; do sleep 3; done
echo "Eureka Server has started"

echo "********************************************************"
echo "Starting Configuration Server"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom \
	 -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI \
	 -jar /usr/local/confsvr/@project.build.finalName@.jar

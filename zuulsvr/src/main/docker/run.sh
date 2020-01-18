#!/bin/sh

echo "********************************************************"
echo "Waiting for the Eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z eurekasvr $EUREKASERVER_PORT`; do sleep 3; done
echo "Eureka Server has started"

echo "********************************************************"
echo "Waiting for the Configuration server to start on port $CONFIGSERVER_PORT"
echo "********************************************************"
while ! `nc -z confsvr $CONFIGSERVER_PORT`; do sleep 3; done
echo "Configuration Server has started"

echo "********************************************************"
echo "Starting Zuul Server"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom \
     -Dspring.profiles.active=$PROFILE \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI \
     -jar /usr/local/zuulsvr/@project.build.finalName@.jar

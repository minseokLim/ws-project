#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "********************************************************"
echo "Waiting for the Eureka server to start on port $(getPort $EUREKASERVER_PORT)"
echo "********************************************************"
while ! `nc -z eurekasvr $(getPort $EUREKASERVER_PORT)`; do sleep 3; done
echo "Eureka Server has started"

echo "********************************************************"
echo "Waiting for the Configuration server to start on port $(getPort $CONFIGSERVER_PORT)"
echo "********************************************************"
while ! `nc -z confsvr $(getPort $CONFIGSERVER_PORT)`; do sleep 3; done
echo "Configuration Server has started"

echo "********************************************************"
echo "Starting Wise Saying Service"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom \
     -Dspring.profiles.active=$PROFILE \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI \
     -jar /usr/local/ws-service/@project.build.finalName@.jar

#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "********************************************************"
echo "Waiting for the Configuration server to start on port $(getPort $CONFIGSERVER_PORT)"
echo "********************************************************"
while ! `nc -z confsvr $(getPort $CONFIGSERVER_PORT)`; do sleep 3; done
echo "Configuration Server has started"

echo "********************************************************"
echo "Waiting for the WS Service to start on port $(getPort $WS_SERVICE_PORT)"
echo "********************************************************"
while ! `nc -z ws-service $(getPort $WS_SERVICE_PORT)`; do sleep 3; done
echo "WS Service has started"

echo "********************************************************"
echo "Waiting for the User Service to start on port $(getPort $USER_SERVICE_PORT)"
echo "********************************************************"
while ! `nc -z user-service $(getPort $USER_SERVICE_PORT)`; do sleep 3; done
echo "User Service has started"

echo "********************************************************"
echo "Starting Batch Service"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom \
     -Dspring.profiles.active=$PROFILE \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI \
     -jar /usr/local/batch-service/@project.build.finalName@.jar

#!/bin/sh

echo "********************************************************"
echo "Waiting for the Configuration server to start on port $CONFIGSERVER_PORT"
echo "********************************************************"
while ! `nc -z confsvr $CONFIGSERVER_PORT`; do sleep 3; done
echo "Configuration Server has started"

echo "********************************************************"
echo "Waiting for the Database server to start on port $DATABASESERVER_PORT"
echo "********************************************************"
while ! `nc -z database $DATABASESERVER_PORT`; do sleep 3; done
echo "Database Server has started"

echo "********************************************************"
echo "Waiting for the WS Service to start on port $WS_SERVICE_PORT"
echo "********************************************************"
while ! `nc -z ws-service $WS_SERVICE_PORT`; do sleep 3; done
echo "WS Service has started"

echo "********************************************************"
echo "Waiting for the User Service to start on port $USER_SERVICE_PORT"
echo "********************************************************"
while ! `nc -z user-service $USER_SERVICE_PORT`; do sleep 3; done
echo "User Service has started"

echo "********************************************************"
echo "Starting Batch Service"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom \
     -Dspring.profiles.active=$PROFILE \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI \
     -jar /usr/local/batch-service/@project.build.finalName@.jar
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
echo "Starting Wise Saying Service"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom \
	 -Dspring.profiles.active=$PROFILE \
	 -Dspring.cloud.config.uri=$CONFIGSERVER_URI \
	 -jar /usr/local/ws-service/@project.build.finalName@.jar

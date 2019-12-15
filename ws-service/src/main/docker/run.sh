#!/bin/sh

echo "********************************************************"
echo "Starting ws-service"
echo "********************************************************"
java -Dspring.profiles.active=$PROFILE \
	-jar /usr/local/ws-service/@project.build.finalName@.jar

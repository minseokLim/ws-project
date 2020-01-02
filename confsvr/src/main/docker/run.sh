#!/bin/sh

echo "********************************************************"
echo "Starting Configuration Server"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom \
     -jar /usr/local/confsvr/@project.build.finalName@.jar

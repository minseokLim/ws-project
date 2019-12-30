#!/bin/sh

echo "********************************************************"
echo "Starting Configuration Server"
echo "********************************************************"
java -jar /usr/local/confsvr/@project.build.finalName@.jar

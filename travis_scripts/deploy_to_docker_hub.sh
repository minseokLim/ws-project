echo "Pushing service docker images to docker hub ...."
docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
docker push mslim8803/authsvr:$BUILD_NAME
# docker push mslim8803/batch-service:$BUILD_NAME
docker push mslim8803/confsvr:$BUILD_NAME
docker push mslim8803/eurekasvr:$BUILD_NAME
docker push mslim8803/user-service:$BUILD_NAME
docker push mslim8803/ws-service:$BUILD_NAME
docker push mslim8803/zuulsvr:$BUILD_NAME

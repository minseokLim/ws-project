echo "Pushing service docker images to docker hub ...."
docker login
docker push mslim8803/authsvr
docker push mslim8803/batch-service
docker push mslim8803/confsvr
docker push mslim8803/eurekasvr
docker push mslim8803/user-service
docker push mslim8803/ws-service
docker push mslim8803/zuulsvr

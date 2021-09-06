TAG_VERSION="latest"
USERHUB="user"
SERVICE_NAME="manager-service"
EXPOSE_PORT=5004
echo "Build Docker image: $SERVICE_NAME"
docker build --rm -t $USERHUB/$SERVICE_NAME:$TAG_VERSION --build-arg SERVICE_NAME=$SERVICE_NAME --build-arg EXPOSE_PORT=$EXPOSE_PORT --no-cache -f Dockerfile .
echo "Pusing Docker Image: $SERVICE_NAME"
docker push $USERHUB/$SERVICE_NAME:$TAG_VERSION
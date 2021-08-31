# DockerHub Tags
TAG_VERSION="latest"
USERHUB="%username%"
# Action
echo "Build client-service"
npm cache clean --force
npm install --force
npm run build
docker build --rm -t $USERHUB/client-service:$TAG_VERSION --build-arg EXPOSE_PORT=3000 --no-cache -f Dockerfile .
echo "Push image docker hub"
docker push $USERHUB/client-service:$TAG_VERSION
echo "Deploy Manager-service with helm chart"
helm install cdo-webclient deployment/cdo-webclient/
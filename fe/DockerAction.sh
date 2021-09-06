SERVICE_NAME="client-service"
echo "Build Docker Image: $SERVICE_NAME"
cd web
npm cache clean --force
npm install --force
npm run build
docker build --rm -t $USERHUB/$SERVICE_NAME:$TAG_VERSION --no-cache -f Dockerfile .
echo "Pusing Docker Image: $SERVICE_NAME"
docker push $USERHUB/$SERVICE_NAME:$TAG_VERSION
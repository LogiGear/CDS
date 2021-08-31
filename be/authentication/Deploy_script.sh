# DockerHub Tags
TAG_VERSION="latest"
USERHUB="%username%"
# Action
echo "Build Authentication-service"
docker build --rm -t $USERHUB/authentication-service:$TAG_VERSION --no-cache -f Dockerfile .
echo "Push image docker hub"
docker push $USERHUB/authentication-service:$TAG_VERSION
echo "Deploy Authentication-service with helm chart"
helm install authentication-service deployment/authentication-service/
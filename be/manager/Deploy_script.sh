# DockerHub Tags
TAG_VERSION="latest"
USERHUB="%username%"
# Action
echo "Build Manager-service"
docker build --rm -t $USERHUB/employees-service:$TAG_VERSION --no-cache -f Dockerfile .
echo "Push image docker hub"
docker push $USERHUB/manager-service:$TAG_VERSION
echo "Deploy Manager-service with helm chart"
helm install manager-service deployment/manager-service/
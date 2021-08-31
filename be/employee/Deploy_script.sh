# DockerHub Tags
TAG_VERSION="latest"
USERHUB="%username%"
# Action
echo "Build Employees-service"
docker build --rm -t $USERHUB/employees-service:$TAG_VERSION --no-cache -f Dockerfile .
echo "Push image docker hub"
docker push $USERHUB/employees-service:$TAG_VERSION
echo "Deploy Employee-service with helm chart"
helm install employees-service deployment/employees-service/
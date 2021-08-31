# DockerHub Tags
TAG_VERSION="latest"
USERHUB="%username%"
# Action
echo "Build Admin-service"
docker build --rm -t $USERHUB/admin-service:$TAG_VERSION --no-cache -f Dockerfile .
echo "Push image docker hub"
docker push $USERHUB/admin-service:$TAG_VERSION
echo "Deploy Admin-service with helm chart"
helm install admin-service deployment/admin-service/
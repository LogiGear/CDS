#Build Docker Image local and push into DockerHub
#Name of Service
AUTHENTICATION_SERVICE="authentication-service"
# EMPLOYEES_SERVICE="employees-service"
# ADMIN_SERVICE="admin-service"
# MANAGER_SERVICE="manager-service"
# CLIENT_SERVICE="client-service"
#DIR of services
DIR_AUTHENTICATION="be/authentication"
DIR_EMPLOYEES="be/employee"
DIR_ADMIN="be/admin"
DIR_MANAGER="be/manager"
#DockerHub Tags
TAG_VERSION="latest"
USERHUB="#YourDockerHubAccount#"

#Backend-Services

if [ -n "$AUTHENTICATION_SERVICE" ] 
then
echo "Build authentication-service"
docker build --rm -t $USERHUB/authentication-service:$TAG_VERSION --build-arg DIR=$DIR_AUTHENTICATION --build-arg SERVICE_NAME=$AUTHENTICATION_SERVICE --build-arg EXPOSE_PORT=5001 --no-cache -f Dockerfile .
echo "Push image docker hub"
docker push $USERHUB/authentication-service:$TAG_VERSION
fi

if [ -n "$EMPLOYEES_SERVICE" ] 
then
echo "Build employees-service"
docker build --rm -t $USERHUB/employees-service:$TAG_VERSION --build-arg DIR=$DIR_EMPLOYEES --build-arg SERVICE_NAME=$EMPLOYEES_SERVICE --build-arg EXPOSE_PORT=5002 --no-cache -f Dockerfile .
echo "Push image docker hub"
docker push $USERHUB/employees-service:$TAG_VERSION
fi

if [ -n "$ADMIN_SERVICE" ] 
then
echo "Build admin-service"
docker build --rm -t $USERHUB/admin-service:$TAG_VERSION --build-arg DIR=$DIR_ADMIN --build-arg SERVICE_NAME=$ADMIN_SERVICE --build-arg EXPOSE_PORT=5003 --no-cache -f Dockerfile .
echo "Push image docker hub"
docker push $USERHUB/admin-service:$TAG_VERSION
fi

if [ -n "$MANAGER_SERVICE" ] 
then
echo "Build manager-service"
docker build --rm -t $USERHUB/manager-service:$TAG_VERSION --build-arg DIR=$DIR_MANAGER --build-arg SERVICE_NAME=$MANAGER_SERVICE --build-arg EXPOSE_PORT=5004 --no-cache -f Dockerfile .
echo "Push image docker hub"
docker push $USERHUB/manager-service:$TAG_VERSION
fi

#Frontend-Services
if [ -n "$CLIENT_SERVICE" ] 
then
echo "Build client-service"
cd fe/web
npm cache clean --force
npm install --force
npm run build
docker build --rm -t $USERHUB/client-service:$TAG_VERSION --build-arg EXPOSE_PORT=3000 --no-cache -f Dockerfile .
echo "Push image docker hub"
docker push $USERHUB/client-service:$TAG_VERSION
cd ../..
fi

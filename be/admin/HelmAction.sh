# Name of service
SERVICE_NAME="admin-service"
# Docker_Hub Username
USER="user"
# Tag version of Docker Image
IMAGE_TAG="latest"
# Tag version of Helm Chart (Deployment)
HELM_TAG="latest"
# Curent Enviroment [Dev/ Deployment]
CURENT_ENV="deployment"
# =======================================
# Service Enviroments
# Database password
postgresPW="Abcd1234"
# Database username 
postgresUN="postgres"
# Database URL
postgresURL="jdbc:postgresql://postgres:5432/postgres"
# ======================================
IMAGE_REPOSITORY="$USER/$SERVICE_NAME"
echo "Using Image: $IMAGE_REPOSITORY"
echo "Deploying $SERVICE_NAME"
Helm install $SERVICE_NAME deployment/ --set image.repository=$IMAGE_REPOSITORY,image.tag=$IMAGE_TAG,env.springProfile=$CURENT_ENV,env.postgresUN=$postgresUN,env.postgresPW=$postgresPW,env.postgresURL=$postgresURL
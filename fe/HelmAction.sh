# Name of service
SERVICE_NAME="cdo-webclient"
# Docker_Hub Username
USER="banhsbao"
# Tag version of Docker Image
IMAGE_TAG="latest"
# Tag version of Helm Chart (Deployment)
HELM_TAG="latest"
# Curent Enviroment [Dev/ Deployment]
CURENT_ENV="deployment"
# ======================================
IMAGE_REPOSITORY="$USER/$SERVICE_NAME"
echo "Using Image: $IMAGE_REPOSITORY"
echo "Deploying $SERVICE_NAME"
Helm install $SERVICE_NAME deployment/ --set image.repository=$IMAGE_REPOSITORY,image.tag=$IMAGE_TAG
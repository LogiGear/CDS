for i in "$@"; do
  case $i in
    -t=*|--tagversion=*)
      IMAGE_TAG="${i#*=}"
      shift
      ;;
    -u=*|--userhub=*)
      USERHUB="${i#*=}"
      shift
      ;;
    *)
      ;;
  esac
done
echo "Current Using: $USERHUB - $IMAGE_TAG"
# Name of service
SERVICE_NAME="client-service"
# Tag version of Docker Image
IMAGE_TAG="latest"
# Tag version of Helm Chart (Deployment)
HELM_TAG="latest"
# Curent Enviroment [Dev/ Deployment]
CURENT_ENV="deployment"
# ======================================
IMAGE_REPOSITORY="$USERHUB/$SERVICE_NAME"
echo "Using Image: $IMAGE_REPOSITORY"
echo "Deploying $SERVICE_NAME"
Helm install $SERVICE_NAME deployment/ --set image.repository=$IMAGE_REPOSITORY,image.tag=$IMAGE_TAG
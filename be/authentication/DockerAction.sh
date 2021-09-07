for i in "$@"; do
  case $i in
    -t=*|--tagversion=*)
      TAG_VERSION="${i#*=}"
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
echo "Current Using: $USERHUB - $TAG_VERSION"
SERVICE_NAME="authentication-service"
EXPOSE_PORT=5001
echo "Build Docker image: $SERVICE_NAME"
docker build --rm -t $USERHUB/$SERVICE_NAME:$TAG_VERSION --build-arg SERVICE_NAME=$SERVICE_NAME --build-arg EXPOSE_PORT=$EXPOSE_PORT --no-cache -f Dockerfile .
echo "Pusing Docker Image: $SERVICE_NAME"
docker push $USERHUB/$SERVICE_NAME:$TAG_VERSION
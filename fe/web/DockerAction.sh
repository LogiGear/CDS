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
SERVICE_NAME="client-service"
echo "Build Docker Image: $SERVICE_NAME"
npm cache clean --force
npm install --force
npm run build
docker build --rm -t $USERHUB/$SERVICE_NAME:$TAG_VERSION --no-cache -f Dockerfile .
echo "Pusing Docker Image: $SERVICE_NAME"
docker push $USERHUB/$SERVICE_NAME:$TAG_VERSION
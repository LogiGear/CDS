echo "Building All Service"
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
echo "Deploy All Service"
cd ..
helm install postgresql deployment/postgresql/
cd be/authentication
.  HelmAction.sh -u=$USERHUB -t=$TAG_VERSION
cd ..
cd employee
.  HelmAction.sh -u=$USERHUB -t=$TAG_VERSION
cd ..
cd admin
.  HelmAction.sh -u=$USERHUB -t=$TAG_VERSION
cd ..
cd manager
.  HelmAction.sh -u=$USERHUB -t=$TAG_VERSION
cd ../..
cd fe/web
.  HelmAction.sh -u=$USERHUB -t=$TAG_VERSION
cd ../..
cd deployment
echo "Press any key to close"
read junk
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
cd ..
cd be
cd authentication
. DockerAction.sh -u=$USERHUB -t=$TAG_VERSION
cd ../
cd employee
. DockerAction.sh -u=$USERHUB -t=$TAG_VERSION
cd ../
cd admin/
. DockerAction.sh -u=$USERHUB -t=$TAG_VERSION
cd ..
cd manager/
. DockerAction.sh -u=$USERHUB -t=$TAG_VERSION
cd ../..
cd fe/web
. DockerAction.sh -u=$USERHUB -t=$TAG_VERSION
cd ../..
echo "Press any key to close"
read junk
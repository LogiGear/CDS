echo "Building All Service"
USERHUB="banhsbao"
TAG_VERSION="latest"
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
cd fe
. DockerAction.sh -u=$USERHUB -t=$TAG_VERSION
cd deployment
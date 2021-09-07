echo "Deploy All Service"
USERHUB="banhsbao"
TAG_VERSION="latest"
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
cd fe
.  HelmAction.sh -u=$USERHUB -t=$TAG_VERSION
cd deployment 
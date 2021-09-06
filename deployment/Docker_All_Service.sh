echo "Building All Service"
cd ..
cd be
cd authentication
. DockerAction.sh
cd ../
cd employee
. DockerAction.sh
cd ../
cd admin/
. DockerAction.sh
cd ..
cd manager/
. DockerAction.sh
cd ../..
cd fe
. DockerAction.sh
cd deployment
echo "Done ^^"
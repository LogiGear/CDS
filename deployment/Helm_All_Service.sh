echo "Deploy All Service"
cd ..
helm install postgresql deployment/helm/postgresql/
sh be/authentication/HelmAction.sh
sh be/employee/HelmAction.sh
sh be/admin/DHelmAction.sh
sh be/manager/HelmAction.sh
sh fe/HelmAction.sh
cd deployment
echo "Done ^^"
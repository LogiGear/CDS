echo "Uninstall Service ..."
helm uninstall postgresql
helm uninstall authentication-service
helm uninstall employee-service
helm uninstall admin-service
helm uninstall manager-service
helm uninstall client-service
echo "Press any key to close"
read junk
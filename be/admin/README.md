# Introduction 
CDO-Admin Service
+ Manage (view/update) user's roles.
+ View all roles
+ View all user

#  Tool / Library
+ eclipse
+ lombok

# Admin Flowchart
![alt text](../doc/CDO-AdminService.jpg)


# REST API
+ **Get all users:** /api/admin/user (ADMIN)
+ **Get all roles:** /api/admin/role (ADMIN)
+ **Get employee with id:** /api/admin/user/{id} (ADMIN)
+ **Get employee with role:** /api/admin/user_role (ADMIN)
+ **Get all employee:** /api/admin/ (ADMIN)
+ **Update employee manager:** /api/admin/updatemanager/{id} (ADMIN)
+ **Update employee cdm:** /api/admin/updatecdm/{id} (ADMIN)

# How to debug
+ Run "mvn package" to build before start debug.
+ Start service "crm-admin-service" as debug mode. The default port is 5003, but you can modify in application.yml.
+ Refer [link](..CDO-Spring-CRM.postman_collection.json) for postman collection to debug this service.

# Introduction 
CDO-Template Service
+ This service should be the bare necessity to create another service in the microservices ecosystem.

#  Tool / Library
+ eclipse
+ lombok

#Config
+ Change the port to another unused port in application.yml to avoid conflicts.
+ Change ALLOWED_TEMPLATE_PATH variable in config/SecurityConfig to allow other API path.
+ config/RoleConfig handle all the role fetching variable from application.yml.
+ config/AppProperties handle all JWT secret key fetching it from application.yml.

#Notice
+ Please keep in mind upon creating another microservices you will have to map it in gateway(Please refer to gateway documentation for more details).
+ Mapping in gateway will take in the value of spring.application.name in application.yml.

# REST API
+ /api/template (USER)

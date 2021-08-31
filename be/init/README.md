# Introduction 
CDO-Init Service
+ This service should in theory drop table and create new table corresponding to models and generate 12000 random data to populate tables.

#  Tool / Library
+ eclipse
+ lombok

#Config
+ Change the port to another unused port in application.yml to avoid conflicts.
+ Delete `ddl-auto: create` before starting service if you wish to only generate data and keep table. (Keep in mind this service work best with default config)

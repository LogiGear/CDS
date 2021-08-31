FROM maven:3.8.1-openjdk-11 AS MAVEN_TOOL_CHAIN
ARG DIR="." \
    SERVICE_NAME="0.0.1-SNAPSHOT" \
    EXPOSE_PORT=5000
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
# With Test
# RUN mvn package

# Without Test
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:11
ARG DIR="." \
    SERVICE_NAME="0.0.1-SNAPSHOT" \
    EXPOSE_PORT=5000
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/manager-service.jar manager-service.jar
RUN echo "#!/bin/bash \n java -jar manager-service.jar" > ./entrypoint.sh
RUN chmod +x ./entrypoint.sh
EXPOSE 5004
ENTRYPOINT ["./entrypoint.sh"]
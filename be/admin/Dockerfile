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
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/admin-service.jar admin-service.jar
RUN echo "${SERVICE_NAME} ${EXPOSE_PORT}"
RUN echo "#!/bin/bash \n java -jar admin-service.jar" > ./entrypoint.sh
RUN chmod +x ./entrypoint.sh
EXPOSE 5003
ENTRYPOINT ["./entrypoint.sh"]
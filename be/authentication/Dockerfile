FROM maven:3.8.1-openjdk-11 AS MAVEN_TOOL_CHAIN
RUN echo "This <3"
ARG SERVICE_NAME="0.0.1-SNAPSHOT" \
    EXPOSE_PORT=5000
COPY ./pom.xml /tmp/
COPY ./src /tmp/src/
WORKDIR /tmp/
# Without Test
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:11
ARG SERVICE_NAME="0.0.1-SNAPSHOT" \
    EXPOSE_PORT=5000
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/${SERVICE_NAME}.jar ${SERVICE_NAME}.jar
RUN echo "${SERVICE_NAME} ${EXPOSE_PORT}"
RUN echo "#!/bin/bash \n java -jar ${SERVICE_NAME}.jar" > ./entrypoint.sh
RUN chmod +x ./entrypoint.sh
EXPOSE ${EXPOSE_PORT}
ENTRYPOINT ["./entrypoint.sh"]
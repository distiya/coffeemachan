FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ARG EXPOSE_PORT
COPY ${JAR_FILE} app.jar
EXPOSE ${EXPOSE_PORT}
ENTRYPOINT ["java","-jar","/app.jar"]
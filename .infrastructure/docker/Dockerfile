FROM eclipse-temurin:24-jdk-alpine AS builder
WORKDIR /usr/app/
COPY . .
RUN ./gradlew assemble --no-daemon

FROM eclipse-temurin:24-jre-alpine

ARG JAR_NAME=*-SNAPSHOT.jar
ARG APP_HOME=/usr/app/

WORKDIR $APP_HOME
COPY --from=builder $APP_HOME .
EXPOSE 8080

ENTRYPOINT ["/bin/sh", "-c", "java -jar /usr/app/build/libs/*-SNAPSHOT.jar"]

FROM gradle:jdk23-alpine AS builder
WORKDIR /usr/app/
COPY . . 
RUN gradle assemble

FROM openjdk:23-jdk-slim-bookworm

ARG JAR_NAME=*-SNAPSHOT.jar
ARG APP_HOME=/usr/app/

WORKDIR $APP_HOME
COPY --from=builder $APP_HOME .
EXPOSE 8080

ENTRYPOINT ["/bin/sh", "-c", "java -jar /usr/app/build/libs/*-SNAPSHOT.jar"]

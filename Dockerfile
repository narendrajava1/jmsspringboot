FROM openjdk:8-alpine

# Required for starting application up.
RUN apk update && apk add /bin/sh

RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} $PROJECT_HOME/app.jar
ENV PORT 9899
EXPOSE 9899
WORKDIR $PROJECT_HOME

ENTRYPOINT java  -jar ./app.jar
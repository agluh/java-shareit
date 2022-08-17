FROM openjdk:17.0.2-jdk-slim-buster
RUN apt upgrade && addgroup gateway --disabled-password && \
    adduser gateway --ingroup gateway && \
    mkdir -p /app
WORKDIR /app
COPY target/*.jar app.jar
RUN chown -R gateway:gateway /app
USER gateway:gateway

ENTRYPOINT java -jar app.jar
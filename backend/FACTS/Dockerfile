ARG BUILD_HOME=/FACTS
FROM gradle:jdk21 AS build-image

ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

#
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src

RUN gradle --no-daemon build



# The version of java used in this container.
FROM openjdk:21-jdk-slim

# Current work directory for the docker container.

# This is to ensure that the current project has been built into a jar and is copied to the Docker folder.

ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
COPY --from=build-image $APP_HOME/build/libs/*.jar .


# Exposes the port 8080 from the containers network to the device's network
EXPOSE 8080:8080

# Runs this command when everything has been set up correctly.
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar"]
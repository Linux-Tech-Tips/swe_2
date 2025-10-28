ARG BUILD_HOME=/tutorial-time

#
# Gradle image for the build stage.
#
FROM gradle:jdk21 AS build-image

#
# Set the working directory.
#
ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

RUN apt-get update
#
# Copy the Gradle config, source code, and static analysis config
# into the build container.
#
COPY --chown=gradle:gradle /tutorial-time/build.gradle.kts /tutorial-time/settings.gradle.kts $APP_HOME/
COPY --chown=gradle:gradle /tutorial-time/src $APP_HOME/src

EXPOSE 8080
CMD ["gradle", "--no-daemon", "bootRun"]

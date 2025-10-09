# The version of java used in this container.
FROM openjdk:21-jdk-slim

# Current work directory for the docker container.
WORKDIR /work

# This is to ensure that the current project has been built into a jar and is copied to the Docker folder.
RUN gradle bootJar && cp ../../../build/libs/app.jar .

# Copies the jar file from the local directory into the
COPY app.jar /work/app.jar

# Exposes the port 8080 from the containers network to the device's network
EXPOSE 8080:8080

# Runs this command when everything has been set up correctly.
ENTRYPOINT ["java", "-jar", "app.jar"]
FROM martin/tt_common:latest AS repo

FROM gradle:jdk21-alpine AS build
COPY --from=repo /repo/backend/tutorial-time /app/backend/tutorial-time
WORKDIR /app/backend/tutorial-time
RUN gradle assemble

FROM alpine/java:21-jdk
COPY --from=build /app/backend/tutorial-time/build/libs /app/build
WORKDIR /app/build

CMD ["java", "-jar", "tutorial-time-0.0.1-SNAPSHOT.jar"]

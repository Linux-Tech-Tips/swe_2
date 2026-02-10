FROM martin/tt_common:latest AS repo

FROM gradle:jdk21-alpine AS build
COPY --from=repo /repo/backend/tutorial-time /app/backend/tutorial-time
WORKDIR /app/backend/tutorial-time
RUN gradle javadoc

FROM nginx:alpine
COPY --from=build /app/backend/tutorial-time/build/docs/javadoc /web/docs/javadoc
COPY default.conf /etc/nginx/conf.d/

EXPOSE 8080

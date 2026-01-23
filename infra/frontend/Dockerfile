FROM martin/tt_common:latest AS repo

FROM node:current-alpine AS build
COPY --from=repo /repo/frontend /app/frontend
WORKDIR /app/frontend
RUN npm install && npm run build

FROM nginx:alpine
COPY --from=build /app/frontend/dist /web/app
COPY default.conf /etc/nginx/conf.d/

EXPOSE 8080

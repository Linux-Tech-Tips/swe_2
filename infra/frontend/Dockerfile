FROM node:current-alpine

RUN apk --no-cache add git openssh

RUN mkdir -p /root/.ssh
ADD github_key /root/.ssh/id_rsa
RUN chmod 400 /root/.ssh/id_rsa

RUN touch /root/.ssh/known_hosts
RUN ssh-keyscan github.com >> /root/.ssh/known_hosts

RUN git clone -b stable git@github.com:Linux-Tech-Tips/swe_2.git /app
WORKDIR /app/frontend

RUN npm install

EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host"]

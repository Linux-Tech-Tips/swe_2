FROM gradle:jdk21

RUN apt-get update
RUN apt-get install -y git ssh openjdk-21-jdk

RUN mkdir -p /root/.ssh
ADD github_key /root/.ssh/id_rsa
RUN chmod 400 /root/.ssh/id_rsa

RUN touch /root/.ssh/known_hosts
RUN ssh-keyscan github.com >> /root/.ssh/known_hosts

RUN git clone -b stable git@github.com:Linux-Tech-Tips/swe_2.git /app
WORKDIR /app/backend/tutorial-time

EXPOSE 8080
CMD ["gradle", "bootRun"]


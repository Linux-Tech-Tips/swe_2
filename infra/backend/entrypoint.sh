#!/bin/sh

# Start docker daemon and wait for it
dockerd-entrypoint.sh &

while ! docker info; do
  sleep 1
done

echo "Docker daemon started"

cd /app/backend
docker compose up --build

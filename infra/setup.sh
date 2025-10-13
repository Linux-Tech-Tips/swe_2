#!/bin/bash

pushd frontend
docker build --no-cache -t martin/frontend .
popd

#pushd backend
#docker build -t martin/backend .
#popd

docker-compose up -d

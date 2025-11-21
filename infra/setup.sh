#!/bin/bash

SCRIPT_DIR=$(dirname $(realpath ${0}))

[[ -z $PRIVATE_PATH ]] && PRIVATE_PATH="$(dirname $(dirname ${SCRIPT_DIR}))/private"

# Setup private keys
cp "${PRIVATE_PATH}/github_key" "${SCRIPT_DIR}/frontend/"
cp "${PRIVATE_PATH}/github_key" "${SCRIPT_DIR}/backend/"

# Setup environment
source "${PRIVATE_PATH}/private_env.sh"

pushd frontend
docker build --build-arg GIT_ACCESS_TOKEN=${GIT_ACCESS_TOKEN} -t martin/frontend .
popd

pushd backend
docker build --build-arg GIT_ACCESS_TOKEN=${GIT_ACCESS_TOKEN} -t martin/backend_wrapper .
popd

# Resetting private keys to not commit them to the repo
echo "PLACEHOLDER" > "${SCRIPT_DIR}/frontend/github_key"
echo "PLACEHOLDER" > "${SCRIPT_DIR}/backend/github_key"

docker-compose up -d

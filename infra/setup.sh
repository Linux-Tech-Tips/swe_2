#!/bin/bash

SCRIPT_DIR=$(dirname $(realpath ${0}))

[[ -z $PRIVATE_PATH ]] && PRIVATE_PATH="$(dirname $(dirname ${SCRIPT_DIR}))/private"

# Setup private keys
cp "${PRIVATE_PATH}/github_key" "${SCRIPT_DIR}/common/"

# Setup environment
source "${PRIVATE_PATH}/private_env.sh"

# Build repo container
pushd common
docker build --build-arg GIT_ACCESS_TOKEN=${GIT_ACCESS_TOKEN} -t martin/tt_common .
popd

# Resetting private keys to not commit them to the repo
echo "PLACEHOLDER" > "${SCRIPT_DIR}/common/github_key"

# Building docker compose project
docker compose build

# Reloading all compose containers
docker compose up -d

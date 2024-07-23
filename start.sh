#!/bin/bash

# Clean the project
./gradlew clean

# Build the project
./gradlew build

DOCKER_IMAGE_NAME=tbnsok/clova
DOCKER_IMAGE_TAG=latest

# Build Docker Image
docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .

# Push to Dockerhub
docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}

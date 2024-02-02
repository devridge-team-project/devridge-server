#!/bin/bash
source ../.env

git submodule update --remote --recursive --init
cd ..
./gradlew clean build
cd api-module
java -Djarmode=layertools -jar build/libs/${JAR_NAME} extract
cd ..
docker-compose up -d --build
#!/bin/bash

git submodule update --init
../gradlew clean build
docker-compose up -d --build
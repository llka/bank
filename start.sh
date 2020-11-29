#!/usr/bin/env bash
./gradlew clean build
./gradlew jibDockerBuild
docker-compose up -d
docker-compose ps

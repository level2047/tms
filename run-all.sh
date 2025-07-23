#!/bin/bash
./gradlew tms-backend:bootJar && ./gradlew tms-frontend:bootJar
docker compose up
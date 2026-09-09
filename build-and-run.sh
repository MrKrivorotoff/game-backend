#!/bin/sh
set -e
mvn clean package -DskipTests=true
docker compose up -d --build
#!/bin/sh

echo "mkdir -p conf logs data"
mkdir -p conf logs data

echo "docker run --name zookeeper ... -d zookeeper"
docker run \
  --name zookeeper \
  --restart always \
  -p 2181:2181 \
  -v $PWD/data:/data \
  -v $PWD/conf:/conf \
  -v $PWD/logs:/datalog \
  -d zookeeper

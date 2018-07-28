#!/bin/sh

echo "mkdir -p conf logs data"
mkdir -p conf logs data

echo "docker run --name consul ... -d consul"
docker run \
  --name consul \
  --restart always \
  -p 8500:8500 \
  -e CONSUL_BIND_INTERFACE=eth0 \
  -v $PWD/data:/consul/data \
  -v $PWD/conf:/consul/config \
  -v $PWD/logs:/var/log \
  -d consul

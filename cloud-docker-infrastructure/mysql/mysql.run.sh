#!/bin/sh

echo "mkdir -p conf logs data and copy my.cnf"
mkdir -p conf logs data
cp -rf my.cnf conf

echo "docker run --name mysql ... -d mysql:5.7"
docker run \
  --name mysql \
  -p 3306:3306 \
  -v $PWD/conf/my.cnf:/etc/mysql/mysql.conf.d/mysqld.cnf \
  -v $PWD/logs:/var/log/mysql \
  -v $PWD/data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -d mysql:5.7

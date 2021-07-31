#!/usr/bin/env bash

# make mymaster image
docker build -f mysql.Dockerfile -t product-write-mysql .

# make myslave image
docker build -f mysql.Dockerfile -t product-read-mysql .

# execute mymaster, myslave container
docker-compose up -d

sleep 20

query="change master to master_host='172.16.0.10', master_port=3306, master_user='slaveuser', master_password='slavepassword', master_log_file='mysql-bin.000003', master_log_pos=154"

mysql -h127.0.0.1 --port 9031 -uroot -e "${query}"
mysql -h127.0.0.1 --port 9031 -uroot -e "start slave"

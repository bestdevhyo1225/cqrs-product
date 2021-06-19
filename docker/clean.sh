docker stop product-write-mysql
docker stop product-read-mysql
docker stop product-rabbitmq
docker stop product-mongo
docker rm product-write-mysql
docker rm product-read-mysql
docker rm product-rabbitmq
docker rm product-mongo
docker rmi product-write-mysql
docker rmi product-read-mysql
docker network rm docker_dock_net

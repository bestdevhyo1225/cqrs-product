docker stop product-write-mysql
docker stop product-read-mysql
docker rm product-write-mysql
docker rm product-read-mysql
docker rmi product-write-mysql
docker rmi product-read-mysql
docker network rm docker_dock_net

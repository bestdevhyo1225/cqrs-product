# Docker Container Stop
docker stop product-write-mysql
docker stop product-read-mysql
docker stop product-rabbitmq
docker stop product-mongo

# Docker Container Remove
docker rm product-write-mysql
docker rm product-read-mysql
docker rm product-rabbitmq
docker rm product-mongo

# Docker Image Remove
docker rmi product-write-mysql
docker rmi product-read-mysql
# shellcheck disable=SC2046
docker rmi $(docker images --filter=reference="mongo" -q)
# shellcheck disable=SC2046
docker rmi $(docker images --filter=reference="rabbitmq" -q)

# Docker Newtork Remove
docker network rm docker_product_mongo_net
docker network rm docker_product_mysql_net
docker network rm docker_product_rabbitmq_net

# Docker Volume Remove All
docker volume prune -f

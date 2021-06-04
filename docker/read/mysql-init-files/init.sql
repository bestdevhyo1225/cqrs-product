CREATE DATABASE product_db character set utf8mb4 collate utf8mb4_general_ci;

CREATE TABLE product_db.temp
(
    id varchar(100) PRIMARY KEY
);

CREATE USER slaveuser@'%' IDENTIFIED BY 'slavepassword';
GRANT ALL PRIVILEGES ON product_db.* TO slaveuser@'%' IDENTIFIED BY 'slavepassword';

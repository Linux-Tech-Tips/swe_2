CREATE DATABASE IF NOT EXISTS springbootdb;

CREATE USER IF NOT EXISTS 'guest'@'%' IDENTIFIED BY "guest";
GRANT ALL PRIVILEGES ON springbootdb.* TO 'guest'@'%';
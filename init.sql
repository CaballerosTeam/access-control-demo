CREATE DATABASE IF NOT EXISTS laboratory DEFAULT CHARACTER SET utf8;
CREATE USER 'db_user'@'localhost' IDENTIFIED BY 'db_password';
GRANT ALL laboratory.* TO 'db_user'@'localhost';

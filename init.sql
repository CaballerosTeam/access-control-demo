CREATE DATABASE IF NOT EXISTS laboratory DEFAULT CHARACTER SET utf8;
CREATE USER 'db_user'@'%' IDENTIFIED BY 'db_password';
GRANT ALL PRIVILEGES ON laboratory.* TO 'db_user'@'%';

CREATE DATABASE IF NOT EXISTS aula_jdbc
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE aula_jdbc;

CREATE TABLE IF NOT EXISTS produtos (
    id         INT            AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100)   NOT NULL,
    preco      DECIMAL(10, 2) NOT NULL,
    quantidade INT            NOT NULL
);

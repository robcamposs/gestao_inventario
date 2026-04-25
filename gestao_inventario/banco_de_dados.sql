-- ============================================================
-- Script de configuração do banco de dados - Gestão de Inventário
-- Execute este script no MySQL Workbench ou via CLI antes de
-- iniciar a aplicação Java.
-- ============================================================

CREATE DATABASE IF NOT EXISTS aula_jdbc
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE aula_jdbc;

CREATE TABLE IF NOT EXISTS produtos (
    id         INT             AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100)    NOT NULL,
    preco      DECIMAL(10, 2)  NOT NULL,
    quantidade INT             NOT NULL
);

-- Dados de exemplo (opcional — remova se preferir começar vazio)
INSERT INTO produtos (nome, preco, quantidade) VALUES
    ('Teclado Mecânico', 349.90,  15),
    ('Mouse Gamer',      189.99,  30),
    ('Monitor 24"',     1299.00,   8),
    ('Headset USB',      229.50,  22),
    ('Webcam Full HD',   399.90,  10);

-- ============================================================
--  ROOTS ACAI PREMIUM – MIGRATION ENTREGA 01
--  Disciplina : Aplicacoes Backend
--  Professor  : Wanderson Pereira dos Santos
--
--  Grupo:
--    Larissa Rodrigues Guimaraes Jales  – 202420927
--    Gabriel Ryan Torres da Silva       – 202510446
--    Maria Yolanda Resende de Araujo    – 202511926
--
--  EXECUTE APOS o script base roots_acai.sql
--
--  Novas entidades:
--    modulos     → lista de funcionalidades do sistema
--    permissoes  → tabela intermediaria Perfil x Modulo
--                  com flags booleanas (pode_visualizar,
--                  pode_editar, pode_excluir)
-- ============================================================

CREATE DATABASE IF NOT EXISTS roots_acai;
USE roots_acai;

-- PRODUTO
CREATE TABLE IF NOT EXISTS produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    preco DOUBLE
);

INSERT INTO produto(nome, preco)
VALUES ('Açaí 500ml', 15.90);

-- PERFIS
CREATE TABLE IF NOT EXISTS perfis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

INSERT INTO perfis(nome) VALUES
('Administrador'),
('Cliente'),
('Preparador'),
('Motoboy');

-- MODULOS
CREATE TABLE IF NOT EXISTS modulos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO modulos (nome, descricao) VALUES
('Financeiro',    'Fluxo de caixa, receitas e despesas'),
('Estoque',       'Controle de ingredientes e produtos'),
('Atendimento',   'Abertura e acompanhamento de pedidos'),
('Relatorios',    'Exportacao de dados e dashboards'),
('Configuracoes', 'Parametros gerais do sistema'),
('Usuarios',      'Gestao de usuarios e perfis');

-- PERMISSOES
CREATE TABLE IF NOT EXISTS permissoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    perfil_id INT NOT NULL,
    modulo_id INT NOT NULL,
    pode_visualizar BOOLEAN NOT NULL DEFAULT FALSE,
    pode_editar BOOLEAN NOT NULL DEFAULT FALSE,
    pode_excluir BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_perm UNIQUE (perfil_id, modulo_id),

    FOREIGN KEY (perfil_id)
        REFERENCES perfis(id)
        ON DELETE CASCADE,

    FOREIGN KEY (modulo_id)
        REFERENCES modulos(id)
        ON DELETE CASCADE
);

-- ADMINISTRADOR TEM ACESSO TOTAL
INSERT INTO permissoes
(perfil_id, modulo_id, pode_visualizar, pode_editar, pode_excluir)
SELECT 1, id, TRUE, TRUE, TRUE
FROM modulos;

-- CLIENTE
INSERT INTO permissoes
(perfil_id, modulo_id, pode_visualizar, pode_editar, pode_excluir)
VALUES
(2, 3, TRUE, FALSE, FALSE);

-- PREPARADOR
INSERT INTO permissoes
(perfil_id, modulo_id, pode_visualizar, pode_editar, pode_excluir)
VALUES
(3, 2, TRUE, TRUE, FALSE),
(3, 3, TRUE, TRUE, FALSE);

-- MOTOBOY
INSERT INTO permissoes
(perfil_id, modulo_id, pode_visualizar, pode_editar, pode_excluir)
VALUES
(4, 3, TRUE, FALSE, FALSE);

-- USUARIOS
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil_id INT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (perfil_id)
        REFERENCES perfis(id)
);

INSERT INTO usuarios
(nome, email, senha, perfil_id)
VALUES
('Administrador', 'admin@rootsacai.com', '123456', 1),
('Cliente Teste', 'cliente@rootsacai.com', '123456', 2),
('Preparador', 'preparador@rootsacai.com', '123456', 3),
('Motoboy', 'motoboy@rootsacai.com', '123456', 4);

-- CONSULTAS DE TESTE
SHOW TABLES;

SELECT * FROM produto;
SELECT * FROM perfis;
SELECT * FROM modulos;
SELECT * FROM permissoes;
SELECT * FROM usuarios;

DESCRIBE produto;
DESCRIBE perfis;
DESCRIBE modulos;
DESCRIBE permissoes;
DESCRIBE usuarios;

-- ============================================================

DROP TABLE IF EXISTS permissoes;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS modulos;
DROP TABLE IF EXISTS perfis;


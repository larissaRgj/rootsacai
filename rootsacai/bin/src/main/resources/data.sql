-- Dados iniciais para teste
-- Execute este arquivo automaticamente ao iniciar a aplicação

-- Inserir usuários de teste
INSERT INTO usuarios (nome, email, senha, perfil_id, ativo) VALUES
('Administrador', 'admin@rootsacai.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DkH0/LvQG8Pn8pK2nH8Z5Z0Z5Z0Z5Z', 1, true),
('Maria Silva', 'maria@rootsacai.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DkH0/LvQG8Pn8pK2nH8Z5Z0Z5Z0Z5Z', 2, true),
('Preparador João', 'joao@rootsacai.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DkH0/LvQG8Pn8pK2nH8Z5Z0Z5Z0Z5Z', 3, true),
('Motoboy Carlos', 'carlos@rootsacai.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DkH0/LvQG8Pn8pK2nH8Z5Z0Z5Z0Z5Z', 4, true);

-- Inserir produtos
INSERT INTO produto (nome, preco) VALUES
('Açaí 300ml', 12.90),
('Açaí 500ml', 15.90),
('Açaí 700ml', 19.90),
('Açaí com Cupuaçu', 18.50),
('Açaí Gourmet Premium', 24.90),
('Bowl Açaí Grande', 28.00);

-- Inserir pedidos de exemplo
INSERT INTO pedidos (tamanho, total, status, cliente_nome, cliente_tel, cliente_id, criado_em) VALUES
('500ml', 17.99, 'aguardando', 'João Costa', '(61) 99999-0001', 2, NOW()),
('300ml', 10.00, 'em_preparo', 'Ana Lima', '(61) 99999-0002', 2, NOW()),
('700ml', 27.00, 'pronto', 'Pedro Souza', '(61) 99999-0003', 2, NOW());

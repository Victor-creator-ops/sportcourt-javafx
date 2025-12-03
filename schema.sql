-- Banco de dados SportCourt
CREATE DATABASE IF NOT EXISTS sportcourt_db;
USE sportcourt_db;

-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'OPERADOR',
    ativo BOOLEAN DEFAULT TRUE,
    nome VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Quadras
CREATE TABLE IF NOT EXISTS quadras (
    id VARCHAR(20) PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    valor_hora DECIMAL(10,2) NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Reservas
CREATE TABLE IF NOT EXISTS reservas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    quadra_id VARCHAR(20) NOT NULL,  -- Alterado para VARCHAR para corresponder a quadras.id
    cliente_nome VARCHAR(100) NOT NULL,
    data DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'AGENDADO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (quadra_id) REFERENCES quadras(id)
);

-- Tabela de Produtos do Bar
CREATE TABLE IF NOT EXISTS produtos_bar (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(50),
    ativo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Comandas (CORRIGIDA - reserva_id pode ser NULL)
CREATE TABLE IF NOT EXISTS comandas_bar (
    id INT PRIMARY KEY AUTO_INCREMENT,
    reserva_id INT NULL,  -- Pode ser NULL
    cliente_nome VARCHAR(100) NULL,
    total DECIMAL(10,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ABERTA',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reserva_id) REFERENCES reservas(id) ON DELETE SET NULL
);

-- Tabela de Itens da Comanda
CREATE TABLE IF NOT EXISTS itens_comanda (
    id INT PRIMARY KEY AUTO_INCREMENT,
    comanda_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL DEFAULT 1,
    valor_unit DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (comanda_id) REFERENCES comandas_bar(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produtos_bar(id)
);

-- Tabela de Financeiro
CREATE TABLE IF NOT EXISTS financeiro (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(20) NOT NULL, -- ENTRADA ou SAIDA
    origem VARCHAR(100) NOT NULL, -- RESERVA, COMANDA, OUTRO
    valor DECIMAL(10,2) NOT NULL,
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descricao TEXT
);

-- Tabela de Materiais Esportivos
CREATE TABLE IF NOT EXISTS materiais_esportivos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(50) NOT NULL,
    valor_locacao DECIMAL(10,2) NOT NULL,
    valor_caucao DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    quantidade_total INT NOT NULL DEFAULT 0,
    quantidade_disponivel INT NOT NULL DEFAULT 0,
    data_validade DATE,
    estado VARCHAR(20) DEFAULT 'BOM',
    ativo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela de Locações de Materiais
CREATE TABLE IF NOT EXISTS locacoes_materiais (
    id INT PRIMARY KEY AUTO_INCREMENT,
    material_id INT NOT NULL,
    reserva_id INT NULL,
    cliente_nome VARCHAR(100) NOT NULL,
    telefone_cliente VARCHAR(20),
    email_cliente VARCHAR(100),
    quantidade INT NOT NULL DEFAULT 1,
    data_abertura TIMESTAMP NOT NULL,
    data_fechamento_previsto TIMESTAMP NOT NULL,
    data_fechamento_real TIMESTAMP NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    valor_caucao DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    multa_atraso DECIMAL(10,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ABERTA',
    checkin_realizado BOOLEAN DEFAULT FALSE,
    observacoes TEXT,
    atendente VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (material_id) REFERENCES materiais_esportivos(id),
    FOREIGN KEY (reserva_id) REFERENCES reservas(id) ON DELETE SET NULL
);

-- Inserir usuário administrador padrão (senha: admin123)
INSERT INTO usuarios (username, password_hash, email, role, nome) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iK6en2B6ZJq3BzRj8cB7iHc7gGzW', 'admin@sportcourt.com', 'ADMIN', 'Administrador')
ON DUPLICATE KEY UPDATE email='admin@sportcourt.com', nome='Administrador';

-- Inserir algumas quadras de exemplo
INSERT INTO quadras (id, tipo, valor_hora, disponivel) VALUES
('Q01', 'Society', 150.00, TRUE),
('Q02', 'Futsal', 120.00, TRUE),
('Q03', 'Vôlei', 100.00, TRUE),
('Q04', 'Tênis', 80.00, TRUE)
ON DUPLICATE KEY UPDATE tipo=VALUES(tipo), valor_hora=VALUES(valor_hora), disponivel=VALUES(disponivel);

-- Inserir alguns produtos de exemplo
INSERT INTO produtos_bar (nome, preco, categoria, ativo) VALUES
('Água Mineral 500ml', 3.50, 'Bebidas', TRUE),
('Refrigerante Lata', 5.00, 'Bebidas', TRUE),
('Cerveja Lata', 8.00, 'Bebidas', TRUE),
('Sanduíche Natural', 12.00, 'Lanches', TRUE),
('Salgado Assado', 6.00, 'Lanches', TRUE)
ON DUPLICATE KEY UPDATE preco=VALUES(preco), categoria=VALUES(categoria), ativo=VALUES(ativo);

INSERT INTO materiais_esportivos (nome, descricao, categoria, valor_locacao, valor_caucao, quantidade_total, quantidade_disponivel, estado) VALUES
('Bola de Futebol Society', 'Bola oficial tamanho 5', 'BOLA', 15.00, 50.00, 10, 10, 'NOVO'),
('Bola de Vôlei', 'Bola oficial Mikasa', 'BOLA', 12.00, 40.00, 8, 8, 'BOM'),
('Rede de Vôlei', 'Rede oficial com 9m', 'REDE', 20.00, 100.00, 4, 4, 'BOM'),
('Coletes', 'Conjunto de 10 coletes', 'UNIFORME', 10.00, 30.00, 5, 5, 'REGULAR'),
('Raquetes de Tênis', 'Par de raquetes Wilson', 'EQUIPAMENTO', 25.00, 80.00, 6, 6, 'BOM'),
('Luvas de Goleiro', 'Tamanho M', 'PROTEÇÃO', 8.00, 25.00, 4, 4, 'NOVO')
ON DUPLICATE KEY UPDATE 
    descricao=VALUES(descricao),
    valor_locacao=VALUES(valor_locacao),
    valor_caucao=VALUES(valor_caucao),
    quantidade_total=VALUES(quantidade_total),
    quantidade_disponivel=VALUES(quantidade_disponivel);

-- Criar índices para melhor performance (removido IF NOT EXISTS para compatibilidade)
DROP INDEX IF EXISTS idx_reservas_data ON reservas;
CREATE INDEX idx_reservas_data ON reservas(data);

DROP INDEX IF EXISTS idx_reservas_status ON reservas;
CREATE INDEX idx_reservas_status ON reservas(status);

DROP INDEX IF EXISTS idx_comandas_status ON comandas_bar;
CREATE INDEX idx_comandas_status ON comandas_bar(status);

DROP INDEX IF EXISTS idx_financeiro_data ON financeiro;
CREATE INDEX idx_financeiro_data ON financeiro(data_hora);


DROP INDEX IF EXISTS idx_materiais_categoria ON materiais_esportivos;
CREATE INDEX idx_materiais_categoria ON materiais_esportivos(categoria);

DROP INDEX IF EXISTS idx_materiais_ativo ON materiais_esportivos;
CREATE INDEX idx_materiais_ativo ON materiais_esportivos(ativo);

DROP INDEX IF EXISTS idx_materiais_disponivel ON materiais_esportivos;
CREATE INDEX idx_materiais_disponivel ON materiais_esportivos(quantidade_disponivel);

DROP INDEX IF EXISTS idx_locacoes_status ON locacoes_materiais;
CREATE INDEX idx_locacoes_status ON locacoes_materiais(status);

DROP INDEX IF EXISTS idx_locacoes_cliente ON locacoes_materiais;
CREATE INDEX idx_locacoes_cliente ON locacoes_materiais(cliente_nome);

DROP INDEX IF EXISTS idx_locacoes_data_prevista ON locacoes_materiais;
CREATE INDEX idx_locacoes_data_prevista ON locacoes_materiais(data_fechamento_previsto);


ALTER TABLE comandas_bar ADD COLUMN IF NOT EXISTS cliente_nome VARCHAR(100) NULL AFTER reserva_id;

CREATE TABLE IF NOT EXISTS usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    senha VARCHAR(64) NOT NULL,
    perfil VARCHAR(20) NOT NULL CHECK (perfil IN ('ADMIN', 'OPERADOR', 'GESTOR')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('ATIVO', 'INATIVO')),
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario (LOWER(email));

-- Senha inicial: admin123
INSERT INTO usuario (nome, email, senha, perfil, status)
VALUES (
    'Administrador GW Frete',
    'admin@gwfrete.com.br',
    '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
    'ADMIN',
    'ATIVO'
)
ON CONFLICT (email) DO NOTHING;

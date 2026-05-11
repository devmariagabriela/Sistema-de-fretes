CREATE TABLE IF NOT EXISTS recuperacao_senha (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    token VARCHAR(255) NOT NULL UNIQUE,
    data_expiracao TIMESTAMP NOT NULL,
    usado BOOLEAN NOT NULL DEFAULT FALSE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_uso TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_recuperacao_senha_token ON recuperacao_senha (token);
CREATE INDEX IF NOT EXISTS idx_recuperacao_senha_usuario_id ON recuperacao_senha (usuario_id);
CREATE INDEX IF NOT EXISTS idx_recuperacao_senha_data_expiracao ON recuperacao_senha (data_expiracao);

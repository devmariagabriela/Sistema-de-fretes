DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'perfil_usuario_enum'
    ) THEN
        CREATE TYPE perfil_usuario_enum AS ENUM (
            'ADMIN',
            'OPERADOR',
            'GESTOR'
        );
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'status_usuario_enum'
    ) THEN
        CREATE TYPE status_usuario_enum AS ENUM (
            'ATIVO',
            'INATIVO',
            'BLOQUEADO'
        );
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil perfil_usuario_enum NOT NULL,
    status status_usuario_enum NOT NULL DEFAULT 'ATIVO',
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario (LOWER(email));

-- Senha inicial: admin123
INSERT INTO usuario (nome, email, senha, perfil, status)
VALUES (
    'Administrador',
    'admin@gwfrete.com',
    '$2a$10$eOVRtAhb7GqkJrfRtcz6a.Na18eJ6./8kAg2FXT9OWikEENWlPPCe',
    'ADMIN',
    'ATIVO'
)
ON CONFLICT (email) DO UPDATE
SET nome = EXCLUDED.nome,
    perfil = EXCLUDED.perfil,
    status = EXCLUDED.status;

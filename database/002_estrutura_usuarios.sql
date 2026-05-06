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

ALTER TABLE usuario
    DROP CONSTRAINT IF EXISTS usuario_perfil_check,
    DROP CONSTRAINT IF EXISTS usuario_status_check;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'usuario'
          AND column_name = 'criado_em'
    ) AND NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'usuario'
          AND column_name = 'data_criacao'
    ) THEN
        ALTER TABLE usuario RENAME COLUMN criado_em TO data_criacao;
    END IF;
END
$$;

ALTER TABLE usuario
    ADD COLUMN IF NOT EXISTS data_criacao TIMESTAMP;

UPDATE usuario
SET data_criacao = CURRENT_TIMESTAMP
WHERE data_criacao IS NULL;

ALTER TABLE usuario
    ALTER COLUMN perfil DROP DEFAULT,
    ALTER COLUMN status DROP DEFAULT;

ALTER TABLE usuario
    ALTER COLUMN nome TYPE VARCHAR(100),
    ALTER COLUMN email TYPE VARCHAR(120),
    ALTER COLUMN senha TYPE VARCHAR(255),
    ALTER COLUMN perfil TYPE perfil_usuario_enum USING perfil::text::perfil_usuario_enum,
    ALTER COLUMN status TYPE status_usuario_enum USING status::text::status_usuario_enum,
    ALTER COLUMN status SET DEFAULT 'ATIVO',
    ALTER COLUMN data_criacao SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN data_criacao SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario (LOWER(email));

-- Senha inicial: admin123
INSERT INTO usuario (nome, email, senha, perfil, status)
VALUES (
    'Administrador',
    'admin@gwfrete.com',
    '$2a$10$DsLsEHuzAcZ/rTTJr/SN7ePy8ivr/H0QSIQH/6K9OoYFTp9JZnUJu',
    'ADMIN',
    'ATIVO'
)
ON CONFLICT (email) DO UPDATE
SET nome = EXCLUDED.nome,
    perfil = EXCLUDED.perfil,
    status = EXCLUDED.status;

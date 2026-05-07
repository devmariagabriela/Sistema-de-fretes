DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'categoria_cnh_enum'
    ) THEN
        CREATE TYPE categoria_cnh_enum AS ENUM (
            'A',
            'B',
            'C',
            'D',
            'E'
        );
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'tipo_vinculo_motorista_enum'
    ) THEN
        CREATE TYPE tipo_vinculo_motorista_enum AS ENUM (
            'FUNCIONARIO',
            'AGREGADO',
            'TERCEIRO'
        );
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'status_motorista_enum'
    ) THEN
        CREATE TYPE status_motorista_enum AS ENUM (
            'ATIVO',
            'INATIVO',
            'SUSPENSO'
        );
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS motorista (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(20),
    cnh_numero VARCHAR(20) NOT NULL UNIQUE,
    cnh_categoria categoria_cnh_enum NOT NULL,
    cnh_validade DATE NOT NULL,
    tipo_vinculo tipo_vinculo_motorista_enum NOT NULL,
    status status_motorista_enum NOT NULL DEFAULT 'ATIVO',
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_motorista_cpf ON motorista (cpf);
CREATE INDEX IF NOT EXISTS idx_motorista_nome ON motorista (nome);
CREATE INDEX IF NOT EXISTS idx_motorista_cnh_numero ON motorista (cnh_numero);

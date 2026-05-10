DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'tipo_cliente_enum'
    ) THEN
        CREATE TYPE tipo_cliente_enum AS ENUM (
            'PESSOA_FISICA',
            'PESSOA_JURIDICA'
        );
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    tipo tipo_cliente_enum NOT NULL,
    cpf_cnpj VARCHAR(18) NOT NULL UNIQUE,
    email VARCHAR(120),
    telefone VARCHAR(20),
    contato VARCHAR(100),
    endereco VARCHAR(255),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(9),
    status BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_cliente_nome ON cliente (nome);
CREATE INDEX IF NOT EXISTS idx_cliente_cpf_cnpj ON cliente (cpf_cnpj);

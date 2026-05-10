DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'status_contrato_enum'
    ) THEN
        CREATE TYPE status_contrato_enum AS ENUM (
            'ATIVO',
            'ENCERRADO',
            'SUSPENSO',
            'CANCELADO'
        );
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS contrato (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES cliente(id),
    numero VARCHAR(30) NOT NULL UNIQUE,
    descricao TEXT,
    valor_mensal NUMERIC(12,2) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE,
    reajuste_percentual NUMERIC(5,2),
    status status_contrato_enum NOT NULL DEFAULT 'ATIVO',
    observacao TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_contrato_numero ON contrato (numero);
CREATE INDEX IF NOT EXISTS idx_contrato_cliente_id ON contrato (cliente_id);
CREATE INDEX IF NOT EXISTS idx_contrato_status ON contrato (status);

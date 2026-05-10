DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'status_fatura_enum'
    ) THEN
        CREATE TYPE status_fatura_enum AS ENUM (
            'PENDENTE',
            'PAGO',
            'VENCIDO',
            'CANCELADO'
        );
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS fatura (
    id BIGSERIAL PRIMARY KEY,
    frete_id BIGINT NOT NULL REFERENCES frete(id),
    cliente_id BIGINT NOT NULL REFERENCES cliente(id),
    numero VARCHAR(30) NOT NULL UNIQUE,
    valor NUMERIC(12,2) NOT NULL,
    data_emissao DATE NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    status status_fatura_enum NOT NULL DEFAULT 'PENDENTE',
    observacao TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_fatura_numero ON fatura (numero);
CREATE INDEX IF NOT EXISTS idx_fatura_status ON fatura (status);
CREATE INDEX IF NOT EXISTS idx_fatura_cliente_id ON fatura (cliente_id);
CREATE INDEX IF NOT EXISTS idx_fatura_frete_id ON fatura (frete_id);

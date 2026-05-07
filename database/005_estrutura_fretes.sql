DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'status_frete_enum'
    ) THEN
        CREATE TYPE status_frete_enum AS ENUM (
            'AGENDADO',
            'EM_COLETA',
            'EM_TRANSITO',
            'ENTREGUE',
            'CANCELADO',
            'OCORRENCIA'
        );
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS frete (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    origem VARCHAR(255) NOT NULL,
    destino VARCHAR(255) NOT NULL,
    descricao_carga TEXT,
    peso_kg NUMERIC(10,2),
    valor_frete NUMERIC(12,2),
    data_saida TIMESTAMP,
    data_entrega TIMESTAMP,
    status status_frete_enum NOT NULL DEFAULT 'AGENDADO',
    motorista_id BIGINT NOT NULL REFERENCES motorista(id),
    veiculo_id BIGINT NOT NULL REFERENCES veiculo(id),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_frete_codigo ON frete (codigo);
CREATE INDEX IF NOT EXISTS idx_frete_status ON frete (status);
CREATE INDEX IF NOT EXISTS idx_frete_data_saida ON frete (data_saida);
CREATE INDEX IF NOT EXISTS idx_frete_data_entrega ON frete (data_entrega);
CREATE INDEX IF NOT EXISTS idx_frete_data_criacao ON frete (data_criacao);

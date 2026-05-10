CREATE TABLE IF NOT EXISTS rastreamento_frete (
    id BIGSERIAL PRIMARY KEY,
    frete_id BIGINT NOT NULL REFERENCES frete(id),
    latitude NUMERIC(10,7),
    longitude NUMERIC(10,7),
    localizacao VARCHAR(255) NOT NULL,
    observacao TEXT,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_rastreamento_frete_frete_id ON rastreamento_frete (frete_id);
CREATE INDEX IF NOT EXISTS idx_rastreamento_frete_data_hora ON rastreamento_frete (data_hora);

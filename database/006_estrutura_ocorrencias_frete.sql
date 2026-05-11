DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'tipo_ocorrencia_frete_enum'
    ) THEN
        CREATE TYPE tipo_ocorrencia_frete_enum AS ENUM (
            'SAIDA_PATIO',
            'EM_ROTA',
            'TENTATIVA_ENTREGA',
            'ENTREGA_REALIZADA',
            'AVARIA',
            'EXTRAVIO',
            'OUTROS'
        );
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS ocorrencia_frete (
    id BIGSERIAL PRIMARY KEY,
    frete_id BIGINT NOT NULL REFERENCES frete(id),
    tipo tipo_ocorrencia_frete_enum NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    localizacao VARCHAR(255) NOT NULL,
    descricao TEXT,
    nome_recebedor VARCHAR(150),
    tipo_documento_recebedor VARCHAR(20),
    documento_recebedor VARCHAR(50),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_ocorrencia_frete_frete_id ON ocorrencia_frete (frete_id);
CREATE INDEX IF NOT EXISTS idx_ocorrencia_frete_data_hora ON ocorrencia_frete (data_hora);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'tipo_notificacao_enum'
    ) THEN
        CREATE TYPE tipo_notificacao_enum AS ENUM (
            'CNH_VENCENDO',
            'VEICULO_MANUTENCAO',
            'FATURA_VENCIDA',
            'FRETE_CANCELADO',
            'OCORRENCIA_CRITICA',
            'OUTROS'
        );
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'status_notificacao_enum'
    ) THEN
        CREATE TYPE status_notificacao_enum AS ENUM (
            'NAO_LIDA',
            'LIDA',
            'ARQUIVADA'
        );
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS notificacao (
    id BIGSERIAL PRIMARY KEY,
    tipo tipo_notificacao_enum NOT NULL,
    status status_notificacao_enum NOT NULL DEFAULT 'NAO_LIDA',
    titulo VARCHAR(150) NOT NULL,
    mensagem TEXT NOT NULL,
    referencia_id BIGINT,
    referencia_tipo VARCHAR(50),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_leitura TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_notificacao_tipo ON notificacao (tipo);
CREATE INDEX IF NOT EXISTS idx_notificacao_status ON notificacao (status);
CREATE INDEX IF NOT EXISTS idx_notificacao_data_criacao ON notificacao (data_criacao);

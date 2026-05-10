DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'tipo_manutencao_enum'
    ) THEN
        CREATE TYPE tipo_manutencao_enum AS ENUM (
            'PREVENTIVA',
            'CORRETIVA'
        );
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'status_manutencao_enum'
    ) THEN
        CREATE TYPE status_manutencao_enum AS ENUM (
            'AGENDADA',
            'EM_ANDAMENTO',
            'CONCLUIDA',
            'CANCELADA'
        );
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS manutencao_veiculo (
    id BIGSERIAL PRIMARY KEY,
    veiculo_id BIGINT NOT NULL REFERENCES veiculo(id),
    tipo tipo_manutencao_enum NOT NULL,
    status status_manutencao_enum NOT NULL DEFAULT 'AGENDADA',
    descricao TEXT NOT NULL,
    oficina VARCHAR(150),
    custo NUMERIC(12,2),
    data_agendada DATE NOT NULL,
    data_inicio DATE,
    data_conclusao DATE,
    quilometragem BIGINT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_manutencao_veiculo_veiculo_id ON manutencao_veiculo (veiculo_id);
CREATE INDEX IF NOT EXISTS idx_manutencao_veiculo_status ON manutencao_veiculo (status);
CREATE INDEX IF NOT EXISTS idx_manutencao_veiculo_data_agendada ON manutencao_veiculo (data_agendada);

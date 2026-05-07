DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'tipo_veiculo_enum'
    ) THEN
        CREATE TYPE tipo_veiculo_enum AS ENUM (
            'CAMINHAO',
            'CARRETA',
            'VUC',
            'UTILITARIO'
        );
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'status_veiculo_enum'
    ) THEN
        CREATE TYPE status_veiculo_enum AS ENUM (
            'DISPONIVEL',
            'EM_ROTA',
            'MANUTENCAO',
            'INATIVO'
        );
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS veiculo (
    id BIGSERIAL PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE,
    modelo VARCHAR(100) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    ano INTEGER NOT NULL,
    capacidade_kg NUMERIC(10,2) NOT NULL,
    tipo tipo_veiculo_enum NOT NULL,
    status status_veiculo_enum NOT NULL DEFAULT 'DISPONIVEL',
    quilometragem BIGINT DEFAULT 0,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_veiculo_placa ON veiculo (placa);

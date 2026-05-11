ALTER TABLE frete
    ADD COLUMN IF NOT EXISTS remetente_id BIGINT REFERENCES cliente(id),
    ADD COLUMN IF NOT EXISTS destinatario_id BIGINT REFERENCES cliente(id);

CREATE INDEX IF NOT EXISTS idx_frete_remetente_id ON frete (remetente_id);
CREATE INDEX IF NOT EXISTS idx_frete_destinatario_id ON frete (destinatario_id);

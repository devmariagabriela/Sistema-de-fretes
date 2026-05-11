ALTER TABLE ocorrencia_frete
ADD COLUMN IF NOT EXISTS tipo_documento_recebedor VARCHAR(20);

CREATE OR REPLACE VIEW vw_kpis_executivos AS
SELECT
    COALESCE((SELECT COUNT(*) FROM frete), 0) AS total_fretes,
    COALESCE((SELECT COUNT(*) FROM frete WHERE status = 'ENTREGUE'), 0) AS fretes_entregues,
    COALESCE((SELECT COUNT(*) FROM frete WHERE status = 'CANCELADO'), 0) AS fretes_cancelados,
    COALESCE((SELECT COUNT(*) FROM frete WHERE status = 'EM_TRANSITO'), 0) AS fretes_em_transito,
    COALESCE((SELECT COUNT(*) FROM cliente), 0) AS total_clientes,
    COALESCE((SELECT COUNT(*) FROM veiculo), 0) AS total_veiculos,
    COALESCE((SELECT COUNT(*) FROM motorista), 0) AS total_motoristas,
    COALESCE((SELECT COUNT(*) FROM fatura), 0) AS total_faturas,
    COALESCE((SELECT SUM(valor) FROM fatura), 0) AS faturamento_total,
    COALESCE((SELECT COUNT(*) FROM fatura WHERE status = 'PENDENTE'), 0) AS faturas_pendentes,
    COALESCE((SELECT COUNT(*) FROM fatura WHERE status = 'VENCIDO'), 0) AS faturas_vencidas,
    COALESCE((SELECT COUNT(*) FROM contrato WHERE status = 'ATIVO'), 0) AS total_contratos_ativos,
    COALESCE((SELECT COUNT(*) FROM ocorrencia_frete), 0) AS total_ocorrencias,
    COALESCE((SELECT COUNT(*) FROM notificacao WHERE status = 'NAO_LIDA'), 0) AS total_notificacoes_nao_lidas,
    COALESCE((SELECT COUNT(*) FROM manutencao_veiculo WHERE status = 'EM_ANDAMENTO'), 0) AS total_manutencoes_em_andamento;

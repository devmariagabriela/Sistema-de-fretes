package br.com.gwfrete.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Fatura implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Frete frete;
    private Cliente cliente;
    private String numero;
    private BigDecimal valor;
    private LocalDate dataEmissao;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private StatusFatura status;
    private String observacao;
    private LocalDateTime dataCriacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Frete getFrete() {
        return frete;
    }

    public void setFrete(Frete frete) {
        this.frete = frete;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public StatusFatura getStatus() {
        return status;
    }

    public void setStatus(StatusFatura status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataEmissaoFormatada() {
        return converterParaDate(dataEmissao);
    }

    public Date getDataVencimentoFormatada() {
        return converterParaDate(dataVencimento);
    }

    public Date getDataPagamentoFormatada() {
        return converterParaDate(dataPagamento);
    }

    public Date getDataCriacaoFormatada() {
        if (dataCriacao == null) {
            return null;
        }

        return Date.from(dataCriacao.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date converterParaDate(LocalDate data) {
        if (data == null) {
            return null;
        }

        return Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

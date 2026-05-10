package br.com.gwfrete.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Contrato implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Cliente cliente;
    private String numero;
    private String descricao;
    private BigDecimal valorMensal;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private BigDecimal reajustePercentual;
    private StatusContrato status;
    private String observacao;
    private LocalDateTime dataCriacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorMensal() {
        return valorMensal;
    }

    public void setValorMensal(BigDecimal valorMensal) {
        this.valorMensal = valorMensal;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public BigDecimal getReajustePercentual() {
        return reajustePercentual;
    }

    public void setReajustePercentual(BigDecimal reajustePercentual) {
        this.reajustePercentual = reajustePercentual;
    }

    public StatusContrato getStatus() {
        return status;
    }

    public void setStatus(StatusContrato status) {
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

    public Date getDataInicioFormatada() {
        return converterParaDate(dataInicio);
    }

    public Date getDataFimFormatada() {
        return converterParaDate(dataFim);
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

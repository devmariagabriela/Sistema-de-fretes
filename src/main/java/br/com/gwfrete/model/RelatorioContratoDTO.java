package br.com.gwfrete.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RelatorioContratoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String numero;
    private String cliente;
    private BigDecimal valorMensal;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusContrato status;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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

    public StatusContrato getStatus() {
        return status;
    }

    public void setStatus(StatusContrato status) {
        this.status = status;
    }

    public Date getDataInicioFormatada() {
        return converterParaDate(dataInicio);
    }

    public Date getDataFimFormatada() {
        return converterParaDate(dataFim);
    }

    private Date converterParaDate(LocalDate data) {
        if (data == null) {
            return null;
        }

        return Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

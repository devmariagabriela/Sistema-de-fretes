package br.com.gwfrete.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RelatorioFinanceiroDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String numero;
    private String cliente;
    private String frete;
    private BigDecimal valor;
    private LocalDate dataEmissao;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private StatusFatura status;

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getFrete() { return frete; }
    public void setFrete(String frete) { this.frete = frete; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public StatusFatura getStatus() { return status; }
    public void setStatus(StatusFatura status) { this.status = status; }

    public Date getDataEmissaoFormatada() { return converterParaDate(dataEmissao); }
    public Date getDataVencimentoFormatada() { return converterParaDate(dataVencimento); }
    public Date getDataPagamentoFormatada() { return converterParaDate(dataPagamento); }

    private Date converterParaDate(LocalDate data) {
        return data == null ? null : Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

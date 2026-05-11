package br.com.gwfrete.relatorio;

import br.com.gwfrete.frete.StatusFrete;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class RelatorioFreteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String remetente;
    private String destinatario;
    private String origem;
    private String destino;
    private String motorista;
    private String veiculo;
    private StatusFrete status;
    private LocalDateTime dataSaida;
    private LocalDateTime dataEntrega;
    private BigDecimal valorFrete;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public StatusFrete getStatus() {
        return status;
    }

    public void setStatus(StatusFrete status) {
        this.status = status;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public Date getDataSaidaFormatada() {
        return converterParaDate(dataSaida);
    }

    public Date getDataEntregaFormatada() {
        return converterParaDate(dataEntrega);
    }

    private Date converterParaDate(LocalDateTime data) {
        if (data == null) {
            return null;
        }

        return Date.from(data.atZone(ZoneId.systemDefault()).toInstant());
    }
}

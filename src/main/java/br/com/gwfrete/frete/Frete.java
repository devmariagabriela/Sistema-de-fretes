package br.com.gwfrete.frete;

import br.com.gwfrete.cliente.Cliente;
import br.com.gwfrete.veiculo.Veiculo;

import br.com.gwfrete.motorista.Motorista;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Frete implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigo;
    private Cliente remetente;
    private Cliente destinatario;
    private String origem;
    private String destino;
    private String descricaoCarga;
    private BigDecimal pesoKg;
    private BigDecimal valorFrete;
    private LocalDateTime dataSaida;
    private LocalDateTime dataEntrega;
    private StatusFrete status;
    private Motorista motorista;
    private Veiculo veiculo;
    private LocalDateTime dataCriacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Cliente getRemetente() {
        return remetente;
    }

    public void setRemetente(Cliente remetente) {
        this.remetente = remetente;
    }

    public Cliente getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Cliente destinatario) {
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

    public String getDescricaoCarga() {
        return descricaoCarga;
    }

    public void setDescricaoCarga(String descricaoCarga) {
        this.descricaoCarga = descricaoCarga;
    }

    public BigDecimal getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(BigDecimal pesoKg) {
        this.pesoKg = pesoKg;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
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

    public StatusFrete getStatus() {
        return status;
    }

    public void setStatus(StatusFrete status) {
        this.status = status;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataSaidaFormatada() {
        return converterParaDate(dataSaida);
    }

    public Date getDataEntregaFormatada() {
        return converterParaDate(dataEntrega);
    }

    public Date getDataCriacaoFormatada() {
        return converterParaDate(dataCriacao);
    }

    public boolean isFinalizado() {
        return status == StatusFrete.ENTREGUE || status == StatusFrete.CANCELADO;
    }

    private Date converterParaDate(LocalDateTime data) {
        if (data == null) {
            return null;
        }

        return Date.from(data.atZone(ZoneId.systemDefault()).toInstant());
    }
}

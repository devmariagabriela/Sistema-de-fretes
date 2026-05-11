package br.com.gwfrete.relatorio;

import br.com.gwfrete.veiculo.TipoVeiculo;

import br.com.gwfrete.veiculo.StatusVeiculo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class RelatorioVeiculoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String placa;
    private String modelo;
    private TipoVeiculo tipo;
    private StatusVeiculo status;
    private BigDecimal capacidade;
    private Long quilometragem;
    private LocalDateTime dataCriacao;

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public TipoVeiculo getTipo() { return tipo; }
    public void setTipo(TipoVeiculo tipo) { this.tipo = tipo; }
    public StatusVeiculo getStatus() { return status; }
    public void setStatus(StatusVeiculo status) { this.status = status; }
    public BigDecimal getCapacidade() { return capacidade; }
    public void setCapacidade(BigDecimal capacidade) { this.capacidade = capacidade; }
    public Long getQuilometragem() { return quilometragem; }
    public void setQuilometragem(Long quilometragem) { this.quilometragem = quilometragem; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public Date getDataCriacaoFormatada() {
        return dataCriacao == null ? null : Date.from(dataCriacao.atZone(ZoneId.systemDefault()).toInstant());
    }
}

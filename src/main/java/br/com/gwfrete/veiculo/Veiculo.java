package br.com.gwfrete.veiculo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Veiculo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String placa;
    private String modelo;
    private String marca;
    private Integer ano;
    private BigDecimal capacidadeKg;
    private TipoVeiculo tipo;
    private StatusVeiculo status;
    private Long quilometragem;
    private LocalDateTime dataCriacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public BigDecimal getCapacidadeKg() {
        return capacidadeKg;
    }

    public void setCapacidadeKg(BigDecimal capacidadeKg) {
        this.capacidadeKg = capacidadeKg;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVeiculo tipo) {
        this.tipo = tipo;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }

    public Long getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(Long quilometragem) {
        this.quilometragem = quilometragem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataCriacaoFormatada() {
        if (dataCriacao == null) {
            return null;
        }

        return Date.from(dataCriacao.atZone(ZoneId.systemDefault()).toInstant());
    }

    public boolean isDisponivel() {
        return status == StatusVeiculo.DISPONIVEL;
    }

    public boolean isEmRota() {
        return status == StatusVeiculo.EM_ROTA;
    }
}

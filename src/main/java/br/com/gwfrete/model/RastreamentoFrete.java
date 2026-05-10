package br.com.gwfrete.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class RastreamentoFrete implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Frete frete;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String localizacao;
    private String observacao;
    private LocalDateTime dataHora;
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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataHoraFormatada() {
        return converterParaDate(dataHora);
    }

    public Date getDataCriacaoFormatada() {
        return converterParaDate(dataCriacao);
    }

    private Date converterParaDate(LocalDateTime data) {
        if (data == null) {
            return null;
        }

        return Date.from(data.atZone(ZoneId.systemDefault()).toInstant());
    }
}

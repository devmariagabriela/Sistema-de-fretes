package br.com.gwfrete.notificacao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Notificacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private TipoNotificacao tipo;
    private StatusNotificacao status;
    private String titulo;
    private String mensagem;
    private Long referenciaId;
    private String referenciaTipo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataLeitura;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public StatusNotificacao getStatus() {
        return status;
    }

    public void setStatus(StatusNotificacao status) {
        this.status = status;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public String getReferenciaTipo() {
        return referenciaTipo;
    }

    public void setReferenciaTipo(String referenciaTipo) {
        this.referenciaTipo = referenciaTipo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(LocalDateTime dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public Date getDataCriacaoFormatada() {
        return converterParaDate(dataCriacao);
    }

    public Date getDataLeituraFormatada() {
        return converterParaDate(dataLeitura);
    }

    private Date converterParaDate(LocalDateTime data) {
        if (data == null) {
            return null;
        }

        return Date.from(data.atZone(ZoneId.systemDefault()).toInstant());
    }
}

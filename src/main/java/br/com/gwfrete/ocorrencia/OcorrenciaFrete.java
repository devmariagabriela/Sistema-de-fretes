package br.com.gwfrete.ocorrencia;

import br.com.gwfrete.frete.Frete;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class OcorrenciaFrete implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Frete frete;
    private TipoOcorrenciaFrete tipo;
    private LocalDateTime dataHora;
    private String localizacao;
    private String descricao;
    private String nomeRecebedor;
    private String documentoRecebedor;
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

    public TipoOcorrenciaFrete getTipo() {
        return tipo;
    }

    public void setTipo(TipoOcorrenciaFrete tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeRecebedor() {
        return nomeRecebedor;
    }

    public void setNomeRecebedor(String nomeRecebedor) {
        this.nomeRecebedor = nomeRecebedor;
    }

    public String getDocumentoRecebedor() {
        return documentoRecebedor;
    }

    public void setDocumentoRecebedor(String documentoRecebedor) {
        this.documentoRecebedor = documentoRecebedor;
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

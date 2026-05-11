package br.com.gwfrete.relatorio;

import br.com.gwfrete.ocorrencia.TipoOcorrenciaFrete;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class RelatorioOcorrenciaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String frete;
    private TipoOcorrenciaFrete tipo;
    private LocalDateTime dataHora;
    private String localizacao;
    private String descricao;
    private String recebedor;

    public String getFrete() { return frete; }
    public void setFrete(String frete) { this.frete = frete; }
    public TipoOcorrenciaFrete getTipo() { return tipo; }
    public void setTipo(TipoOcorrenciaFrete tipo) { this.tipo = tipo; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getRecebedor() { return recebedor; }
    public void setRecebedor(String recebedor) { this.recebedor = recebedor; }

    public Date getDataHoraFormatada() {
        return dataHora == null ? null : Date.from(dataHora.atZone(ZoneId.systemDefault()).toInstant());
    }
}

package br.com.gwfrete.relatorio;

import br.com.gwfrete.manutencao.TipoManutencao;

import br.com.gwfrete.manutencao.StatusManutencao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RelatorioManutencaoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String veiculo;
    private TipoManutencao tipo;
    private StatusManutencao status;
    private String descricao;
    private String oficina;
    private BigDecimal custo;
    private LocalDate dataAgendada;
    private LocalDate dataConclusao;

    public String getVeiculo() { return veiculo; }
    public void setVeiculo(String veiculo) { this.veiculo = veiculo; }
    public TipoManutencao getTipo() { return tipo; }
    public void setTipo(TipoManutencao tipo) { this.tipo = tipo; }
    public StatusManutencao getStatus() { return status; }
    public void setStatus(StatusManutencao status) { this.status = status; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getOficina() { return oficina; }
    public void setOficina(String oficina) { this.oficina = oficina; }
    public BigDecimal getCusto() { return custo; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }
    public LocalDate getDataAgendada() { return dataAgendada; }
    public void setDataAgendada(LocalDate dataAgendada) { this.dataAgendada = dataAgendada; }
    public LocalDate getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao = dataConclusao; }

    public Date getDataAgendadaFormatada() { return converterParaDate(dataAgendada); }
    public Date getDataConclusaoFormatada() { return converterParaDate(dataConclusao); }

    private Date converterParaDate(LocalDate data) {
        return data == null ? null : Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

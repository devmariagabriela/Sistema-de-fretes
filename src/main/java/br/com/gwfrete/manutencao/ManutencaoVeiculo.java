package br.com.gwfrete.manutencao;

import br.com.gwfrete.veiculo.Veiculo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ManutencaoVeiculo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Veiculo veiculo;
    private TipoManutencao tipo;
    private StatusManutencao status;
    private String descricao;
    private String oficina;
    private BigDecimal custo;
    private LocalDate dataAgendada;
    private LocalDate dataInicio;
    private LocalDate dataConclusao;
    private Long quilometragem;
    private LocalDateTime dataCriacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public TipoManutencao getTipo() {
        return tipo;
    }

    public void setTipo(TipoManutencao tipo) {
        this.tipo = tipo;
    }

    public StatusManutencao getStatus() {
        return status;
    }

    public void setStatus(StatusManutencao status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public BigDecimal getCusto() {
        return custo;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }

    public LocalDate getDataAgendada() {
        return dataAgendada;
    }

    public void setDataAgendada(LocalDate dataAgendada) {
        this.dataAgendada = dataAgendada;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
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

    public Date getDataAgendadaFormatada() {
        return converterParaDate(dataAgendada);
    }

    public Date getDataInicioFormatada() {
        return converterParaDate(dataInicio);
    }

    public Date getDataConclusaoFormatada() {
        return converterParaDate(dataConclusao);
    }

    public Date getDataCriacaoFormatada() {
        if (dataCriacao == null) {
            return null;
        }

        return Date.from(dataCriacao.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date converterParaDate(LocalDate data) {
        if (data == null) {
            return null;
        }

        return Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

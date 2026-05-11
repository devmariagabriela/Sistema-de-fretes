package br.com.gwfrete.motorista;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Motorista implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String cnhNumero;
    private CategoriaCNH cnhCategoria;
    private LocalDate cnhValidade;
    private TipoVinculoMotorista tipoVinculo;
    private StatusMotorista status;
    private LocalDateTime dataCriacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCnhNumero() {
        return cnhNumero;
    }

    public void setCnhNumero(String cnhNumero) {
        this.cnhNumero = cnhNumero;
    }

    public CategoriaCNH getCnhCategoria() {
        return cnhCategoria;
    }

    public void setCnhCategoria(CategoriaCNH cnhCategoria) {
        this.cnhCategoria = cnhCategoria;
    }

    public LocalDate getCnhValidade() {
        return cnhValidade;
    }

    public void setCnhValidade(LocalDate cnhValidade) {
        this.cnhValidade = cnhValidade;
    }

    public TipoVinculoMotorista getTipoVinculo() {
        return tipoVinculo;
    }

    public void setTipoVinculo(TipoVinculoMotorista tipoVinculo) {
        this.tipoVinculo = tipoVinculo;
    }

    public StatusMotorista getStatus() {
        return status;
    }

    public void setStatus(StatusMotorista status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataNascimentoFormatada() {
        return converterParaDate(dataNascimento);
    }

    public Date getCnhValidadeFormatada() {
        return converterParaDate(cnhValidade);
    }

    public Date getDataCriacaoFormatada() {
        if (dataCriacao == null) {
            return null;
        }

        return Date.from(dataCriacao.atZone(ZoneId.systemDefault()).toInstant());
    }

    public boolean isCnhVencida() {
        return cnhValidade != null && cnhValidade.isBefore(LocalDate.now());
    }

    public boolean isDisponivelOperacionalmente() {
        return status == StatusMotorista.ATIVO && !isCnhVencida();
    }

    private Date converterParaDate(LocalDate data) {
        if (data == null) {
            return null;
        }

        return Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

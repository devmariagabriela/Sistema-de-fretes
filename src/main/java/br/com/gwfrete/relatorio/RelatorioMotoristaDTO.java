package br.com.gwfrete.relatorio;

import br.com.gwfrete.motorista.TipoVinculoMotorista;

import br.com.gwfrete.motorista.StatusMotorista;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RelatorioMotoristaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String cpf;
    private String telefone;
    private String cnh;
    private String categoriaCnh;
    private LocalDate validadeCnh;
    private TipoVinculoMotorista vinculo;
    private StatusMotorista status;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getCnh() { return cnh; }
    public void setCnh(String cnh) { this.cnh = cnh; }
    public String getCategoriaCnh() { return categoriaCnh; }
    public void setCategoriaCnh(String categoriaCnh) { this.categoriaCnh = categoriaCnh; }
    public LocalDate getValidadeCnh() { return validadeCnh; }
    public void setValidadeCnh(LocalDate validadeCnh) { this.validadeCnh = validadeCnh; }
    public TipoVinculoMotorista getVinculo() { return vinculo; }
    public void setVinculo(TipoVinculoMotorista vinculo) { this.vinculo = vinculo; }
    public StatusMotorista getStatus() { return status; }
    public void setStatus(StatusMotorista status) { this.status = status; }
    public Date getValidadeCnhFormatada() {
        return validadeCnh == null ? null : Date.from(validadeCnh.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

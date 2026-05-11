package br.com.gwfrete.model;

import java.io.Serializable;

public class RelatorioClienteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private TipoCliente tipo;
    private String cpfCnpj;
    private String email;
    private String telefone;
    private String cidade;
    private String estado;
    private Boolean status;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public TipoCliente getTipo() { return tipo; }
    public void setTipo(TipoCliente tipo) { this.tipo = tipo; }
    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
    public String getStatusDescricao() { return Boolean.TRUE.equals(status) ? "Ativo" : "Inativo"; }
}

package br.com.gwfrete.usuario;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private PerfilUsuario perfil;
    private StatusUsuario status;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public void setStatus(StatusUsuario status) {
        this.status = status;
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

    public boolean isAtivo() {
        return status == StatusUsuario.ATIVO;
    }

    public void setAtivo(boolean ativo) {
        this.status = ativo ? StatusUsuario.ATIVO : StatusUsuario.INATIVO;
    }

    public boolean possuiPerfil(PerfilUsuario perfil) {
        return this.perfil == perfil;
    }
}

package br.com.gwfrete.usuario;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RecuperacaoSenha implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Usuario usuario;
    private String token;
    private LocalDateTime dataExpiracao;
    private Boolean usado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public Boolean getUsado() {
        return usado;
    }

    public void setUsado(Boolean usado) {
        this.usado = usado;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataUso() {
        return dataUso;
    }

    public void setDataUso(LocalDateTime dataUso) {
        this.dataUso = dataUso;
    }
}

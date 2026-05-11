package br.com.gwfrete.dashboard;

import java.io.Serializable;

public class DashboardDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalFretes;
    private int fretesEmTransito;
    private int fretesEntregues;
    private int fretesCancelados;
    private int totalVeiculos;
    private int veiculosDisponiveis;
    private int veiculosEmManutencao;
    private int totalMotoristas;
    private int motoristasAtivos;
    private int totalOcorrencias;

    public int getTotalFretes() {
        return totalFretes;
    }

    public void setTotalFretes(int totalFretes) {
        this.totalFretes = totalFretes;
    }

    public int getFretesEmTransito() {
        return fretesEmTransito;
    }

    public void setFretesEmTransito(int fretesEmTransito) {
        this.fretesEmTransito = fretesEmTransito;
    }

    public int getFretesEntregues() {
        return fretesEntregues;
    }

    public void setFretesEntregues(int fretesEntregues) {
        this.fretesEntregues = fretesEntregues;
    }

    public int getFretesCancelados() {
        return fretesCancelados;
    }

    public void setFretesCancelados(int fretesCancelados) {
        this.fretesCancelados = fretesCancelados;
    }

    public int getTotalVeiculos() {
        return totalVeiculos;
    }

    public void setTotalVeiculos(int totalVeiculos) {
        this.totalVeiculos = totalVeiculos;
    }

    public int getVeiculosDisponiveis() {
        return veiculosDisponiveis;
    }

    public void setVeiculosDisponiveis(int veiculosDisponiveis) {
        this.veiculosDisponiveis = veiculosDisponiveis;
    }

    public int getVeiculosEmManutencao() {
        return veiculosEmManutencao;
    }

    public void setVeiculosEmManutencao(int veiculosEmManutencao) {
        this.veiculosEmManutencao = veiculosEmManutencao;
    }

    public int getTotalMotoristas() {
        return totalMotoristas;
    }

    public void setTotalMotoristas(int totalMotoristas) {
        this.totalMotoristas = totalMotoristas;
    }

    public int getMotoristasAtivos() {
        return motoristasAtivos;
    }

    public void setMotoristasAtivos(int motoristasAtivos) {
        this.motoristasAtivos = motoristasAtivos;
    }

    public int getTotalOcorrencias() {
        return totalOcorrencias;
    }

    public void setTotalOcorrencias(int totalOcorrencias) {
        this.totalOcorrencias = totalOcorrencias;
    }
}

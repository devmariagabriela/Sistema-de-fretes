package br.com.gwfrete.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class KPIExecutivoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalFretes;
    private int fretesEntregues;
    private int fretesCancelados;
    private int fretesEmTransito;
    private int totalClientes;
    private int totalVeiculos;
    private int totalMotoristas;
    private int totalFaturas;
    private BigDecimal faturamentoTotal = BigDecimal.ZERO;
    private int faturasPendentes;
    private int faturasVencidas;
    private int totalContratosAtivos;
    private int totalOcorrencias;
    private int totalNotificacoesNaoLidas;
    private int totalManutencoesEmAndamento;

    public int getTotalFretes() {
        return totalFretes;
    }

    public void setTotalFretes(int totalFretes) {
        this.totalFretes = totalFretes;
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

    public int getFretesEmTransito() {
        return fretesEmTransito;
    }

    public void setFretesEmTransito(int fretesEmTransito) {
        this.fretesEmTransito = fretesEmTransito;
    }

    public int getTotalClientes() {
        return totalClientes;
    }

    public void setTotalClientes(int totalClientes) {
        this.totalClientes = totalClientes;
    }

    public int getTotalVeiculos() {
        return totalVeiculos;
    }

    public void setTotalVeiculos(int totalVeiculos) {
        this.totalVeiculos = totalVeiculos;
    }

    public int getTotalMotoristas() {
        return totalMotoristas;
    }

    public void setTotalMotoristas(int totalMotoristas) {
        this.totalMotoristas = totalMotoristas;
    }

    public int getTotalFaturas() {
        return totalFaturas;
    }

    public void setTotalFaturas(int totalFaturas) {
        this.totalFaturas = totalFaturas;
    }

    public BigDecimal getFaturamentoTotal() {
        return faturamentoTotal;
    }

    public void setFaturamentoTotal(BigDecimal faturamentoTotal) {
        this.faturamentoTotal = faturamentoTotal;
    }

    public int getFaturasPendentes() {
        return faturasPendentes;
    }

    public void setFaturasPendentes(int faturasPendentes) {
        this.faturasPendentes = faturasPendentes;
    }

    public int getFaturasVencidas() {
        return faturasVencidas;
    }

    public void setFaturasVencidas(int faturasVencidas) {
        this.faturasVencidas = faturasVencidas;
    }

    public int getTotalContratosAtivos() {
        return totalContratosAtivos;
    }

    public void setTotalContratosAtivos(int totalContratosAtivos) {
        this.totalContratosAtivos = totalContratosAtivos;
    }

    public int getTotalOcorrencias() {
        return totalOcorrencias;
    }

    public void setTotalOcorrencias(int totalOcorrencias) {
        this.totalOcorrencias = totalOcorrencias;
    }

    public int getTotalNotificacoesNaoLidas() {
        return totalNotificacoesNaoLidas;
    }

    public void setTotalNotificacoesNaoLidas(int totalNotificacoesNaoLidas) {
        this.totalNotificacoesNaoLidas = totalNotificacoesNaoLidas;
    }

    public int getTotalManutencoesEmAndamento() {
        return totalManutencoesEmAndamento;
    }

    public void setTotalManutencoesEmAndamento(int totalManutencoesEmAndamento) {
        this.totalManutencoesEmAndamento = totalManutencoesEmAndamento;
    }
}

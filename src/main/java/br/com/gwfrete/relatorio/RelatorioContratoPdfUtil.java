package br.com.gwfrete.relatorio;

import br.com.gwfrete.relatorio.RelatorioContratoDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class RelatorioContratoPdfUtil {
    private static final String CAMINHO_RELATORIO = "report/relatorio_contratos.jrxml";

    private RelatorioContratoPdfUtil() {
    }

    public static byte[] gerarPdf(List<RelatorioContratoDTO> contratos) throws JRException {
        JasperPrint impressao = preencherRelatorio(contratos);
        return JasperExportManager.exportReportToPdf(impressao);
    }

    public static byte[] gerarXlsx(List<RelatorioContratoDTO> contratos) throws JRException {
        return RelatorioExcelUtil.gerarContratos(contratos);
    }

    private static JasperPrint preencherRelatorio(List<RelatorioContratoDTO> contratos) throws JRException {
        JasperReport relatorioCompilado = compilarRelatorio();
        Map<String, Object> parametros = criarParametros(contratos);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contratos);

        return JasperFillManager.fillReport(relatorioCompilado, parametros, dataSource);
    }

    private static JasperReport compilarRelatorio() throws JRException {
        InputStream inputStream = RelatorioContratoPdfUtil.class.getClassLoader().getResourceAsStream(CAMINHO_RELATORIO);

        if (inputStream == null) {
            throw new JRException("Arquivo JRXML do relatório de contratos não encontrado.");
        }

        return JasperCompileManager.compileReport(inputStream);
    }

    private static Map<String, Object> criarParametros(List<RelatorioContratoDTO> contratos) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("GERADO_EM", new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR")).format(new Date()));
        parametros.put("INDICADOR_1_LABEL", "CONTRATOS");
        parametros.put("INDICADOR_1_VALOR", String.valueOf(contratos == null ? 0 : contratos.size()));
        parametros.put("INDICADOR_2_LABEL", "ATIVOS");
        parametros.put("INDICADOR_2_VALOR", String.valueOf(contarPorStatus(contratos, "ATIVO")));
        parametros.put("INDICADOR_3_LABEL", "SUSPENSOS");
        parametros.put("INDICADOR_3_VALOR", String.valueOf(contarPorStatus(contratos, "SUSPENSO")));
        parametros.put("INDICADOR_4_LABEL", "VALOR MENSAL");
        parametros.put("INDICADOR_4_VALOR", NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(somarValorMensal(contratos)));
        parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        return parametros;
    }

    private static int contarPorStatus(List<RelatorioContratoDTO> contratos, String status) {
        if (contratos == null) {
            return 0;
        }

        int total = 0;
        for (RelatorioContratoDTO contrato : contratos) {
            if (contrato != null && contrato.getStatus() != null && status.equals(contrato.getStatus().name())) {
                total++;
            }
        }
        return total;
    }

    private static BigDecimal somarValorMensal(List<RelatorioContratoDTO> contratos) {
        if (contratos == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (RelatorioContratoDTO contrato : contratos) {
            if (contrato != null && contrato.getValorMensal() != null) {
                total = total.add(contrato.getValorMensal());
            }
        }
        return total;
    }
}

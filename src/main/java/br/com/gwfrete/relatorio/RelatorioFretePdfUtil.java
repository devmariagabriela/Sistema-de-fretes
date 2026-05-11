package br.com.gwfrete.relatorio;

import br.com.gwfrete.relatorio.RelatorioFreteDTO;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class RelatorioFretePdfUtil {
    private static final String CAMINHO_RELATORIO = "report/relatorio_fretes.jrxml";

    private RelatorioFretePdfUtil() {
    }

    public static byte[] gerarPdf(List<RelatorioFreteDTO> fretes) throws JRException {
        JasperReport relatorioCompilado = compilarRelatorio();
        Map<String, Object> parametros = criarParametros(fretes);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(fretes);

        JasperPrint impressao = JasperFillManager.fillReport(relatorioCompilado, parametros, dataSource);
        return JasperExportManager.exportReportToPdf(impressao);
    }

    private static JasperReport compilarRelatorio() throws JRException {
        InputStream inputStream = RelatorioFretePdfUtil.class.getClassLoader().getResourceAsStream(CAMINHO_RELATORIO);

        if (inputStream == null) {
            throw new JRException("Arquivo JRXML do relatório de fretes não encontrado.");
        }

        return JasperCompileManager.compileReport(inputStream);
    }

    private static Map<String, Object> criarParametros(List<RelatorioFreteDTO> fretes) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("GERADO_EM", new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR")).format(new Date()));
        parametros.put("TOTAL_FRETES", fretes == null ? 0 : fretes.size());
        parametros.put("TOTAL_ENTREGUES", contarPorStatus(fretes, "ENTREGUE"));
        parametros.put("TOTAL_EM_TRANSITO", contarPorStatus(fretes, "EM_TRANSITO"));
        parametros.put("TOTAL_VALOR", somarValorFrete(fretes));
        parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        return parametros;
    }

    private static int contarPorStatus(List<RelatorioFreteDTO> fretes, String status) {
        if (fretes == null) {
            return 0;
        }

        int total = 0;
        for (RelatorioFreteDTO frete : fretes) {
            if (frete != null && frete.getStatus() != null && status.equals(frete.getStatus().name())) {
                total++;
            }
        }
        return total;
    }

    private static BigDecimal somarValorFrete(List<RelatorioFreteDTO> fretes) {
        if (fretes == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (RelatorioFreteDTO frete : fretes) {
            if (frete != null && frete.getValorFrete() != null) {
                total = total.add(frete.getValorFrete());
            }
        }
        return total;
    }
}

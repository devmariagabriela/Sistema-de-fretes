package br.com.gwfrete.util;

import br.com.gwfrete.model.RelatorioContratoDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
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
        JasperReport relatorioCompilado = compilarRelatorio();
        Map<String, Object> parametros = criarParametros();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contratos);

        JasperPrint impressao = JasperFillManager.fillReport(relatorioCompilado, parametros, dataSource);
        return JasperExportManager.exportReportToPdf(impressao);
    }

    private static JasperReport compilarRelatorio() throws JRException {
        InputStream inputStream = RelatorioContratoPdfUtil.class.getClassLoader().getResourceAsStream(CAMINHO_RELATORIO);

        if (inputStream == null) {
            throw new JRException("Arquivo JRXML do relatório de contratos não encontrado.");
        }

        return JasperCompileManager.compileReport(inputStream);
    }

    private static Map<String, Object> criarParametros() {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("GERADO_EM", new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR")).format(new Date()));
        parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        return parametros;
    }
}

package br.com.gwfrete.util;

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

public final class RelatorioPdfUtil {
    private RelatorioPdfUtil() {
    }

    public static byte[] gerarPdf(String caminhoRelatorio, List<?> dados) throws JRException {
        JasperReport relatorioCompilado = compilarRelatorio(caminhoRelatorio);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);
        JasperPrint impressao = JasperFillManager.fillReport(relatorioCompilado, criarParametros(), dataSource);
        return JasperExportManager.exportReportToPdf(impressao);
    }

    private static JasperReport compilarRelatorio(String caminhoRelatorio) throws JRException {
        InputStream inputStream = RelatorioPdfUtil.class.getClassLoader().getResourceAsStream(caminhoRelatorio);

        if (inputStream == null) {
            throw new JRException("Arquivo JRXML não encontrado: " + caminhoRelatorio);
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

package br.com.gwfrete.relatorio;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
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
        JasperPrint impressao = JasperFillManager.fillReport(relatorioCompilado, criarParametros(caminhoRelatorio, dados), dataSource);
        return JasperExportManager.exportReportToPdf(impressao);
    }

    private static JasperReport compilarRelatorio(String caminhoRelatorio) throws JRException {
        InputStream inputStream = RelatorioPdfUtil.class.getClassLoader().getResourceAsStream(caminhoRelatorio);

        if (inputStream == null) {
            throw new JRException("Arquivo JRXML não encontrado: " + caminhoRelatorio);
        }

        return JasperCompileManager.compileReport(inputStream);
    }

    private static Map<String, Object> criarParametros(String caminhoRelatorio, List<?> dados) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("GERADO_EM", new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR")).format(new Date()));
        prepararIndicadores(parametros, caminhoRelatorio, dados);
        parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        return parametros;
    }

    private static void prepararIndicadores(Map<String, Object> parametros, String caminhoRelatorio, List<?> dados) {
        int total = dados == null ? 0 : dados.size();

        if (caminhoRelatorio.contains("financeiro")) {
            adicionarIndicadores(parametros, "FATURAS", String.valueOf(total), "PAGAS", String.valueOf(contarEnum(dados, "getStatus", "PAGO")),
                    "PENDENTES", String.valueOf(contarEnum(dados, "getStatus", "PENDENTE")), "VALOR TOTAL", formatarMoeda(somarBigDecimal(dados, "getValor")));
            return;
        }

        if (caminhoRelatorio.contains("manutencoes")) {
            adicionarIndicadores(parametros, "MANUTENÇÕES", String.valueOf(total), "CONCLUÍDAS", String.valueOf(contarEnum(dados, "getStatus", "CONCLUIDA")),
                    "EM ANDAMENTO", String.valueOf(contarEnum(dados, "getStatus", "EM_ANDAMENTO")), "CUSTO TOTAL", formatarMoeda(somarBigDecimal(dados, "getCusto")));
            return;
        }

        if (caminhoRelatorio.contains("ocorrencias")) {
            adicionarIndicadores(parametros, "OCORRÊNCIAS", String.valueOf(total), "ENTREGAS", String.valueOf(contarEnum(dados, "getTipo", "ENTREGA_REALIZADA")),
                    "AVARIAS", String.valueOf(contarEnum(dados, "getTipo", "AVARIA")), "COM RECEBEDOR", String.valueOf(contarPreenchidos(dados, "getRecebedor")));
            return;
        }

        if (caminhoRelatorio.contains("clientes")) {
            adicionarIndicadores(parametros, "CLIENTES", String.valueOf(total), "ATIVOS", String.valueOf(contarTexto(dados, "getStatusDescricao", "Ativo")),
                    "PESSOA JURÍDICA", String.valueOf(contarEnum(dados, "getTipo", "PESSOA_JURIDICA")), "ESTADOS", String.valueOf(contarDistintos(dados, "getEstado")));
            return;
        }

        if (caminhoRelatorio.contains("motoristas")) {
            adicionarIndicadores(parametros, "MOTORISTAS", String.valueOf(total), "ATIVOS", String.valueOf(contarEnum(dados, "getStatus", "ATIVO")),
                    "SUSPENSOS", String.valueOf(contarEnum(dados, "getStatus", "SUSPENSO")), "CNH CADASTRADAS", String.valueOf(contarPreenchidos(dados, "getCnh")));
            return;
        }

        if (caminhoRelatorio.contains("veiculos")) {
            adicionarIndicadores(parametros, "VEÍCULOS", String.valueOf(total), "DISPONÍVEIS", String.valueOf(contarEnum(dados, "getStatus", "DISPONIVEL")),
                    "EM ROTA", String.valueOf(contarEnum(dados, "getStatus", "EM_ROTA")), "CAPACIDADE TOTAL", formatarDecimal(somarBigDecimal(dados, "getCapacidade")));
            return;
        }

        adicionarIndicadores(parametros, "REGISTROS", String.valueOf(total), "ATIVOS", "0", "PENDENTES", "0", "TOTAL", "0");
    }

    private static void adicionarIndicadores(Map<String, Object> parametros, String label1, String valor1, String label2, String valor2,
            String label3, String valor3, String label4, String valor4) {
        parametros.put("INDICADOR_1_LABEL", label1);
        parametros.put("INDICADOR_1_VALOR", valor1);
        parametros.put("INDICADOR_2_LABEL", label2);
        parametros.put("INDICADOR_2_VALOR", valor2);
        parametros.put("INDICADOR_3_LABEL", label3);
        parametros.put("INDICADOR_3_VALOR", valor3);
        parametros.put("INDICADOR_4_LABEL", label4);
        parametros.put("INDICADOR_4_VALOR", valor4);
    }

    private static int contarEnum(List<?> dados, String getter, String valorEnum) {
        int total = 0;
        if (dados == null) {
            return total;
        }
        for (Object item : dados) {
            Object valor = invocar(item, getter);
            if (valor instanceof Enum && valorEnum.equals(((Enum<?>) valor).name())) {
                total++;
            }
        }
        return total;
    }

    private static int contarTexto(List<?> dados, String getter, String esperado) {
        int total = 0;
        if (dados == null) {
            return total;
        }
        for (Object item : dados) {
            Object valor = invocar(item, getter);
            if (esperado.equals(valor)) {
                total++;
            }
        }
        return total;
    }

    private static int contarPreenchidos(List<?> dados, String getter) {
        int total = 0;
        if (dados == null) {
            return total;
        }
        for (Object item : dados) {
            Object valor = invocar(item, getter);
            if (valor != null && !valor.toString().trim().isEmpty()) {
                total++;
            }
        }
        return total;
    }

    private static int contarDistintos(List<?> dados, String getter) {
        HashSet<String> valores = new HashSet<>();
        if (dados == null) {
            return 0;
        }
        for (Object item : dados) {
            Object valor = invocar(item, getter);
            if (valor != null && !valor.toString().trim().isEmpty()) {
                valores.add(valor.toString().trim());
            }
        }
        return valores.size();
    }

    private static BigDecimal somarBigDecimal(List<?> dados, String getter) {
        BigDecimal total = BigDecimal.ZERO;
        if (dados == null) {
            return total;
        }
        for (Object item : dados) {
            Object valor = invocar(item, getter);
            if (valor instanceof BigDecimal) {
                total = total.add((BigDecimal) valor);
            }
        }
        return total;
    }

    private static Object invocar(Object item, String getter) {
        if (item == null) {
            return null;
        }
        try {
            Method metodo = item.getClass().getMethod(getter);
            return metodo.invoke(item);
        } catch (Exception e) {
            return null;
        }
    }

    private static String formatarMoeda(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor == null ? BigDecimal.ZERO : valor);
    }

    private static String formatarDecimal(BigDecimal valor) {
        return NumberFormat.getNumberInstance(new Locale("pt", "BR")).format(valor == null ? BigDecimal.ZERO : valor);
    }
}

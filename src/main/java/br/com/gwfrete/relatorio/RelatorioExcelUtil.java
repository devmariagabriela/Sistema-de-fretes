package br.com.gwfrete.relatorio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

final class RelatorioExcelUtil {
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private RelatorioExcelUtil() {
    }

    static byte[] gerarFretes(List<RelatorioFreteDTO> fretes) {
        return gerar("Fretes", Arrays.asList(
                coluna("Código", new Valor<RelatorioFreteDTO>() { public Object obter(RelatorioFreteDTO item) { return item.getCodigo(); } }),
                coluna("Origem", new Valor<RelatorioFreteDTO>() { public Object obter(RelatorioFreteDTO item) { return item.getOrigem(); } }),
                coluna("Destino", new Valor<RelatorioFreteDTO>() { public Object obter(RelatorioFreteDTO item) { return item.getDestino(); } }),
                coluna("Motorista", new Valor<RelatorioFreteDTO>() { public Object obter(RelatorioFreteDTO item) { return item.getMotorista(); } }),
                coluna("Veículo", new Valor<RelatorioFreteDTO>() { public Object obter(RelatorioFreteDTO item) { return item.getVeiculo(); } }),
                coluna("Status", new Valor<RelatorioFreteDTO>() { public Object obter(RelatorioFreteDTO item) { return item.getStatus(); } }),
                coluna("Data saída", new Valor<RelatorioFreteDTO>() { public Object obter(RelatorioFreteDTO item) { return item.getDataSaida(); } }),
                coluna("Data entrega", new Valor<RelatorioFreteDTO>() { public Object obter(RelatorioFreteDTO item) { return item.getDataEntrega(); } }),
                coluna("Valor frete", new Valor<RelatorioFreteDTO>() { public Object obter(RelatorioFreteDTO item) { return item.getValorFrete(); } })
        ), fretes);
    }

    static byte[] gerarContratos(List<RelatorioContratoDTO> contratos) {
        return gerar("Contratos", Arrays.asList(
                coluna("Número", new Valor<RelatorioContratoDTO>() { public Object obter(RelatorioContratoDTO item) { return item.getNumero(); } }),
                coluna("Cliente", new Valor<RelatorioContratoDTO>() { public Object obter(RelatorioContratoDTO item) { return item.getCliente(); } }),
                coluna("Valor mensal", new Valor<RelatorioContratoDTO>() { public Object obter(RelatorioContratoDTO item) { return item.getValorMensal(); } }),
                coluna("Data início", new Valor<RelatorioContratoDTO>() { public Object obter(RelatorioContratoDTO item) { return item.getDataInicio(); } }),
                coluna("Data fim", new Valor<RelatorioContratoDTO>() { public Object obter(RelatorioContratoDTO item) { return item.getDataFim(); } }),
                coluna("Status", new Valor<RelatorioContratoDTO>() { public Object obter(RelatorioContratoDTO item) { return item.getStatus(); } })
        ), contratos);
    }

    static byte[] gerarFinanceiro(List<RelatorioFinanceiroDTO> faturas) {
        return gerar("Financeiro", Arrays.asList(
                coluna("Número", new Valor<RelatorioFinanceiroDTO>() { public Object obter(RelatorioFinanceiroDTO item) { return item.getNumero(); } }),
                coluna("Cliente", new Valor<RelatorioFinanceiroDTO>() { public Object obter(RelatorioFinanceiroDTO item) { return item.getCliente(); } }),
                coluna("Frete", new Valor<RelatorioFinanceiroDTO>() { public Object obter(RelatorioFinanceiroDTO item) { return item.getFrete(); } }),
                coluna("Valor", new Valor<RelatorioFinanceiroDTO>() { public Object obter(RelatorioFinanceiroDTO item) { return item.getValor(); } }),
                coluna("Data emissão", new Valor<RelatorioFinanceiroDTO>() { public Object obter(RelatorioFinanceiroDTO item) { return item.getDataEmissao(); } }),
                coluna("Data vencimento", new Valor<RelatorioFinanceiroDTO>() { public Object obter(RelatorioFinanceiroDTO item) { return item.getDataVencimento(); } }),
                coluna("Data pagamento", new Valor<RelatorioFinanceiroDTO>() { public Object obter(RelatorioFinanceiroDTO item) { return item.getDataPagamento(); } }),
                coluna("Status", new Valor<RelatorioFinanceiroDTO>() { public Object obter(RelatorioFinanceiroDTO item) { return item.getStatus(); } })
        ), faturas);
    }

    static byte[] gerarManutencoes(List<RelatorioManutencaoDTO> manutencoes) {
        return gerar("Manutenções", Arrays.asList(
                coluna("Veículo", new Valor<RelatorioManutencaoDTO>() { public Object obter(RelatorioManutencaoDTO item) { return item.getVeiculo(); } }),
                coluna("Tipo", new Valor<RelatorioManutencaoDTO>() { public Object obter(RelatorioManutencaoDTO item) { return item.getTipo(); } }),
                coluna("Status", new Valor<RelatorioManutencaoDTO>() { public Object obter(RelatorioManutencaoDTO item) { return item.getStatus(); } }),
                coluna("Descrição", new Valor<RelatorioManutencaoDTO>() { public Object obter(RelatorioManutencaoDTO item) { return item.getDescricao(); } }),
                coluna("Oficina", new Valor<RelatorioManutencaoDTO>() { public Object obter(RelatorioManutencaoDTO item) { return item.getOficina(); } }),
                coluna("Custo", new Valor<RelatorioManutencaoDTO>() { public Object obter(RelatorioManutencaoDTO item) { return item.getCusto(); } }),
                coluna("Data agendada", new Valor<RelatorioManutencaoDTO>() { public Object obter(RelatorioManutencaoDTO item) { return item.getDataAgendada(); } }),
                coluna("Data conclusão", new Valor<RelatorioManutencaoDTO>() { public Object obter(RelatorioManutencaoDTO item) { return item.getDataConclusao(); } })
        ), manutencoes);
    }

    static byte[] gerarOcorrencias(List<RelatorioOcorrenciaDTO> ocorrencias) {
        return gerar("Ocorrências", Arrays.asList(
                coluna("Frete", new Valor<RelatorioOcorrenciaDTO>() { public Object obter(RelatorioOcorrenciaDTO item) { return item.getFrete(); } }),
                coluna("Tipo", new Valor<RelatorioOcorrenciaDTO>() { public Object obter(RelatorioOcorrenciaDTO item) { return item.getTipo(); } }),
                coluna("Data/hora", new Valor<RelatorioOcorrenciaDTO>() { public Object obter(RelatorioOcorrenciaDTO item) { return item.getDataHora(); } }),
                coluna("Localização", new Valor<RelatorioOcorrenciaDTO>() { public Object obter(RelatorioOcorrenciaDTO item) { return item.getLocalizacao(); } }),
                coluna("Descrição", new Valor<RelatorioOcorrenciaDTO>() { public Object obter(RelatorioOcorrenciaDTO item) { return item.getDescricao(); } }),
                coluna("Recebedor", new Valor<RelatorioOcorrenciaDTO>() { public Object obter(RelatorioOcorrenciaDTO item) { return item.getRecebedor(); } })
        ), ocorrencias);
    }

    static byte[] gerarClientes(List<RelatorioClienteDTO> clientes) {
        return gerar("Clientes", Arrays.asList(
                coluna("Nome", new Valor<RelatorioClienteDTO>() { public Object obter(RelatorioClienteDTO item) { return item.getNome(); } }),
                coluna("Tipo", new Valor<RelatorioClienteDTO>() { public Object obter(RelatorioClienteDTO item) { return item.getTipo(); } }),
                coluna("CPF/CNPJ", new Valor<RelatorioClienteDTO>() { public Object obter(RelatorioClienteDTO item) { return item.getCpfCnpj(); } }),
                coluna("E-mail", new Valor<RelatorioClienteDTO>() { public Object obter(RelatorioClienteDTO item) { return item.getEmail(); } }),
                coluna("Telefone", new Valor<RelatorioClienteDTO>() { public Object obter(RelatorioClienteDTO item) { return item.getTelefone(); } }),
                coluna("Cidade", new Valor<RelatorioClienteDTO>() { public Object obter(RelatorioClienteDTO item) { return item.getCidade(); } }),
                coluna("Estado", new Valor<RelatorioClienteDTO>() { public Object obter(RelatorioClienteDTO item) { return item.getEstado(); } }),
                coluna("Status", new Valor<RelatorioClienteDTO>() { public Object obter(RelatorioClienteDTO item) { return item.getStatusDescricao(); } })
        ), clientes);
    }

    static byte[] gerarMotoristas(List<RelatorioMotoristaDTO> motoristas) {
        return gerar("Motoristas", Arrays.asList(
                coluna("Nome", new Valor<RelatorioMotoristaDTO>() { public Object obter(RelatorioMotoristaDTO item) { return item.getNome(); } }),
                coluna("CPF", new Valor<RelatorioMotoristaDTO>() { public Object obter(RelatorioMotoristaDTO item) { return item.getCpf(); } }),
                coluna("Telefone", new Valor<RelatorioMotoristaDTO>() { public Object obter(RelatorioMotoristaDTO item) { return item.getTelefone(); } }),
                coluna("CNH", new Valor<RelatorioMotoristaDTO>() { public Object obter(RelatorioMotoristaDTO item) { return item.getCnh(); } }),
                coluna("Categoria CNH", new Valor<RelatorioMotoristaDTO>() { public Object obter(RelatorioMotoristaDTO item) { return item.getCategoriaCnh(); } }),
                coluna("Validade CNH", new Valor<RelatorioMotoristaDTO>() { public Object obter(RelatorioMotoristaDTO item) { return item.getValidadeCnh(); } }),
                coluna("Vínculo", new Valor<RelatorioMotoristaDTO>() { public Object obter(RelatorioMotoristaDTO item) { return item.getVinculo(); } }),
                coluna("Status", new Valor<RelatorioMotoristaDTO>() { public Object obter(RelatorioMotoristaDTO item) { return item.getStatus(); } })
        ), motoristas);
    }

    static byte[] gerarVeiculos(List<RelatorioVeiculoDTO> veiculos) {
        return gerar("Veículos", Arrays.asList(
                coluna("Placa", new Valor<RelatorioVeiculoDTO>() { public Object obter(RelatorioVeiculoDTO item) { return item.getPlaca(); } }),
                coluna("Modelo", new Valor<RelatorioVeiculoDTO>() { public Object obter(RelatorioVeiculoDTO item) { return item.getModelo(); } }),
                coluna("Tipo", new Valor<RelatorioVeiculoDTO>() { public Object obter(RelatorioVeiculoDTO item) { return item.getTipo(); } }),
                coluna("Status", new Valor<RelatorioVeiculoDTO>() { public Object obter(RelatorioVeiculoDTO item) { return item.getStatus(); } }),
                coluna("Capacidade", new Valor<RelatorioVeiculoDTO>() { public Object obter(RelatorioVeiculoDTO item) { return item.getCapacidade(); } }),
                coluna("Quilometragem", new Valor<RelatorioVeiculoDTO>() { public Object obter(RelatorioVeiculoDTO item) { return item.getQuilometragem(); } }),
                coluna("Data criação", new Valor<RelatorioVeiculoDTO>() { public Object obter(RelatorioVeiculoDTO item) { return item.getDataCriacao(); } })
        ), veiculos);
    }

    @SuppressWarnings("unchecked")
    static byte[] gerarPorRelatorio(String caminhoRelatorio, List<?> dados) {
        if (caminhoRelatorio.contains("financeiro")) return gerarFinanceiro((List<RelatorioFinanceiroDTO>) dados);
        if (caminhoRelatorio.contains("manutencoes")) return gerarManutencoes((List<RelatorioManutencaoDTO>) dados);
        if (caminhoRelatorio.contains("ocorrencias")) return gerarOcorrencias((List<RelatorioOcorrenciaDTO>) dados);
        if (caminhoRelatorio.contains("clientes")) return gerarClientes((List<RelatorioClienteDTO>) dados);
        if (caminhoRelatorio.contains("motoristas")) return gerarMotoristas((List<RelatorioMotoristaDTO>) dados);
        if (caminhoRelatorio.contains("veiculos")) return gerarVeiculos((List<RelatorioVeiculoDTO>) dados);
        return gerar("Relatório", Arrays.asList(
                coluna("Registro", new Valor<Object>() { public Object obter(Object item) { return item; } })
        ), (List<Object>) dados);
    }

    private static <T> Coluna<T> coluna(String titulo, Valor<T> valor) {
        return new Coluna<T>(titulo, valor);
    }

    private static <T> byte[] gerar(String nomeAba, List<Coluna<T>> colunas, List<T> dados) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(bytes);
            adicionar(zip, "[Content_Types].xml", contentTypes());
            adicionar(zip, "_rels/.rels", rels());
            adicionar(zip, "xl/workbook.xml", workbook(nomeAba));
            adicionar(zip, "xl/_rels/workbook.xml.rels", workbookRels());
            adicionar(zip, "xl/styles.xml", styles());
            adicionar(zip, "xl/worksheets/sheet1.xml", sheet(colunas, dados));
            zip.close();
            return bytes.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível gerar a planilha Excel.", e);
        }
    }

    private static void adicionar(ZipOutputStream zip, String caminho, String conteudo) throws IOException {
        zip.putNextEntry(new ZipEntry(caminho));
        zip.write(conteudo.getBytes("UTF-8"));
        zip.closeEntry();
    }

    private static <T> String sheet(List<Coluna<T>> colunas, List<T> dados) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        xml.append("<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
        xml.append("<sheetViews><sheetView workbookViewId=\"0\"><pane ySplit=\"1\" topLeftCell=\"A2\" activePane=\"bottomLeft\" state=\"frozen\"/></sheetView></sheetViews>");
        xml.append("<sheetData>");
        xml.append("<row r=\"1\">");
        for (int i = 0; i < colunas.size(); i++) {
            texto(xml, i + 1, 1, colunas.get(i).titulo, 1);
        }
        xml.append("</row>");
        if (dados != null) {
            for (int linha = 0; linha < dados.size(); linha++) {
                xml.append("<row r=\"").append(linha + 2).append("\">");
                T item = dados.get(linha);
                for (int coluna = 0; coluna < colunas.size(); coluna++) {
                    valor(xml, coluna + 1, linha + 2, colunas.get(coluna).valor.obter(item));
                }
                xml.append("</row>");
            }
        }
        xml.append("</sheetData>");
        xml.append("<autoFilter ref=\"A1:").append(referencia(colunas.size(), Math.max(1, dados == null ? 1 : dados.size() + 1))).append("\"/>");
        xml.append("</worksheet>");
        return xml.toString();
    }

    private static void valor(StringBuilder xml, int coluna, int linha, Object valor) {
        if (valor == null) {
            texto(xml, coluna, linha, "", 0);
            return;
        }
        if (valor instanceof BigDecimal) {
            numero(xml, coluna, linha, ((BigDecimal) valor).toPlainString());
            return;
        }
        if (valor instanceof Number) {
            numero(xml, coluna, linha, valor.toString());
            return;
        }
        if (valor instanceof LocalDate) {
            texto(xml, coluna, linha, DATA.format((LocalDate) valor), 0);
            return;
        }
        if (valor instanceof LocalDateTime) {
            texto(xml, coluna, linha, DATA_HORA.format((LocalDateTime) valor), 0);
            return;
        }
        texto(xml, coluna, linha, descricao(valor), 0);
    }

    private static void numero(StringBuilder xml, int coluna, int linha, String valor) {
        xml.append("<c r=\"").append(referencia(coluna, linha)).append("\" s=\"2\"><v>").append(valor).append("</v></c>");
    }

    private static void texto(StringBuilder xml, int coluna, int linha, String valor, int estilo) {
        xml.append("<c r=\"").append(referencia(coluna, linha)).append("\" t=\"inlineStr\"");
        if (estilo > 0) {
            xml.append(" s=\"").append(estilo).append("\"");
        }
        xml.append("><is><t>").append(escapar(valor)).append("</t></is></c>");
    }

    private static String descricao(Object valor) {
        try {
            Method metodo = valor.getClass().getMethod("getDescricao");
            Object descricao = metodo.invoke(valor);
            return descricao == null ? "" : descricao.toString();
        } catch (Exception e) {
            if (valor instanceof Enum) {
                return ((Enum<?>) valor).name();
            }
            return valor.toString();
        }
    }

    private static String referencia(int coluna, int linha) {
        StringBuilder letras = new StringBuilder();
        int numero = coluna;
        while (numero > 0) {
            int resto = (numero - 1) % 26;
            letras.insert(0, (char) ('A' + resto));
            numero = (numero - 1) / 26;
        }
        return letras.append(linha).toString();
    }

    private static String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&apos;");
    }

    private static String contentTypes() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">"
                + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>"
                + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>"
                + "<Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "<Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/>"
                + "</Types>";
    }

    private static String rels() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/>"
                + "</Relationships>";
    }

    private static String workbook(String nomeAba) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
                + "<sheets><sheet name=\"" + escapar(nomeAba) + "\" sheetId=\"1\" r:id=\"rId1\"/></sheets>"
                + "</workbook>";
    }

    private static String workbookRels() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet1.xml\"/>"
                + "<Relationship Id=\"rId2\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>"
                + "</Relationships>";
    }

    private static String styles() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">"
                + "<fonts count=\"2\"><font><sz val=\"11\"/><name val=\"Calibri\"/></font><font><b/><sz val=\"11\"/><name val=\"Calibri\"/><color rgb=\"FFFFFFFF\"/></font></fonts>"
                + "<fills count=\"3\"><fill><patternFill patternType=\"none\"/></fill><fill><patternFill patternType=\"gray125\"/></fill><fill><patternFill patternType=\"solid\"><fgColor rgb=\"FF0F2747\"/><bgColor indexed=\"64\"/></patternFill></fill></fills>"
                + "<borders count=\"1\"><border><left/><right/><top/><bottom/><diagonal/></border></borders>"
                + "<cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs>"
                + "<cellXfs count=\"3\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/><xf numFmtId=\"0\" fontId=\"1\" fillId=\"2\" borderId=\"0\" xfId=\"0\" applyFont=\"1\" applyFill=\"1\"/><xf numFmtId=\"4\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\" applyNumberFormat=\"1\"/></cellXfs>"
                + "</styleSheet>";
    }

    private interface Valor<T> {
        Object obter(T item);
    }

    private static final class Coluna<T> {
        private final String titulo;
        private final Valor<T> valor;

        private Coluna(String titulo, Valor<T> valor) {
            this.titulo = titulo;
            this.valor = valor;
        }
    }
}

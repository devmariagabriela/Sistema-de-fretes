package br.com.gwfrete.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexaoFactory {
    private static final String URL_PADRAO = "jdbc:postgresql://localhost:5432/gwfrete";
    private static final String USUARIO_PADRAO = "postgres";
    private static final String SENHA_PADRAO = "1234";

    private ConexaoFactory() {
    }

    public static Connection obterConexao() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado.", e);
        }

        return DriverManager.getConnection(
                obterConfiguracao("gwfrete.db.url", "GWFRETE_DB_URL", URL_PADRAO),
                obterConfiguracao("gwfrete.db.usuario", "GWFRETE_DB_USUARIO", USUARIO_PADRAO),
                obterConfiguracao("gwfrete.db.senha", "GWFRETE_DB_SENHA", SENHA_PADRAO));
    }

    private static String obterConfiguracao(String propriedade, String variavelAmbiente, String valorPadrao) {
        String valor = System.getProperty(propriedade);

        if (valor != null && !valor.trim().isEmpty()) {
            return valor;
        }

        valor = System.getenv(variavelAmbiente);

        if (valor != null && !valor.trim().isEmpty()) {
            return valor;
        }

        return valorPadrao;
    }
}

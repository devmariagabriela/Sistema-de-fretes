package br.com.gwfrete.util;

import org.mindrot.jbcrypt.BCrypt;

public final class CriptografiaUtil {
    private static final int CUSTO_BCRYPT = 10;

    private CriptografiaUtil() {
    }

    public static String gerarHashBCrypt(String valor) {
        return BCrypt.hashpw(valor, BCrypt.gensalt(CUSTO_BCRYPT));
    }

    public static boolean verificarSenhaBCrypt(String senhaInformada, String hashArmazenado) {
        if (senhaInformada == null || hashArmazenado == null || hashArmazenado.trim().isEmpty()) {
            return false;
        }

        try {
            return BCrypt.checkpw(senhaInformada, hashArmazenado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

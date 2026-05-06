package br.com.gwfrete.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class CriptografiaUtil {
    private CriptografiaUtil() {
    }

    public static String gerarSha256(String valor) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(valor.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexadecimal = new StringBuilder();

            for (byte b : hash) {
                hexadecimal.append(String.format("%02x", b));
            }

            return hexadecimal.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Não foi possível criptografar a senha.", e);
        }
    }
}

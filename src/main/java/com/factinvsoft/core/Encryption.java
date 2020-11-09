package com.factinvsoft.core;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author RichardZarama
 */
public class Encryption {
    public static String Encriptar(final String texto) {
        
            try {
                final MessageDigest md = MessageDigest.getInstance("SHA-256");
                final byte[] hashInBytes = md.digest(texto.getBytes(StandardCharsets.UTF_8));
                return new String(Hex.encode(hashInBytes));
            } catch (java.security.NoSuchAlgorithmException e) {
                return "";
            }
        
    }
}

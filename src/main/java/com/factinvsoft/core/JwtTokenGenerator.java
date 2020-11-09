package com.factinvsoft.core;

import static com.nimbusds.jose.JOSEObjectType.JWT;
import static com.nimbusds.jose.JWSAlgorithm.RS256;
import static com.nimbusds.jwt.JWTClaimsSet.parse;
import static java.lang.Thread.currentThread;
import static net.minidev.json.parser.JSONParser.DEFAULT_PERMISSIVE_MODE;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.SignedJWT;

import org.eclipse.microprofile.jwt.Claims;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

public class JwtTokenGenerator {

    public static String generateJWTString(final String jsonResource) throws Exception {

        final JSONParser parser = new JSONParser(DEFAULT_PERMISSIVE_MODE);
        final JSONObject jwtJson = (JSONObject) parser.parse(jsonResource);

        final long currentTimeInSecs = (System.currentTimeMillis() / 1000);
        final long expirationTime = currentTimeInSecs + (24 * 60 * 60);

        jwtJson.put(Claims.iat.name(), currentTimeInSecs);
        jwtJson.put(Claims.auth_time.name(), currentTimeInSecs);
        jwtJson.put(Claims.exp.name(), expirationTime);
        jwtJson.put(Claims.jti.name(), UUID.randomUUID().toString());
        jwtJson.put(Claims.iss.name(), "factinvsoft");
        jwtJson.put(Claims.aud.name(), "factinvsoft");

        final SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(RS256).keyID("/privateKey.pem").type(JWT).build(), parse(jwtJson));

        signedJWT.sign(new RSASSASigner(readPrivateKey("privateKey.pem")));

        return signedJWT.serialize();
    }

    public static PrivateKey readPrivateKey(final String resourceName) throws Exception {
        final byte[] byteBuffer = new byte[16384];
        final int length = currentThread().getContextClassLoader().getResource(resourceName).openStream()
                .read(byteBuffer);

        final String key = new String(byteBuffer, 0, length).replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)----", "").replaceAll("\r\n", "").replaceAll("\n", "").trim();

        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key)));
    }
}

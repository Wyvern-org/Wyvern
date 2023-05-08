package com.oldschoolminecraft.nemesis.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public final class Jwt
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Jwt.class);

    /**
     * The algorithm for signing JWT.
     */
    private static final Algorithm ALGORITHM = Algorithm.HMAC512(getHmacKey());

    /**
     * The verified for JWTs.
     */
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM)
            .withIssuer("Owen2k6")
            .build();

    /**
     * Create a JWT.
     *
     * @param uuid  The user to create the token for.
     * @param expires When the token expires.
     * @return The created JWT token.
     */
    public static String createToken(String uuid, String username, int permissions, long expires) {
        return JWT.create()
                .withIssuer("Owen2k6")
                .withClaim("uuid", uuid)
                .withClaim("username", username)
                .withClaim("permissions", permissions)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.ofEpochMilli(System.currentTimeMillis() + expires))
                .sign(ALGORITHM);
    }

    public static String createToken(long expiration, String... claims) {
        com.auth0.jwt.JWTCreator.Builder builder = JWT.create();
        builder.withIssuer("Owen2k6");

        for (int i = 0; i < claims.length; i += 2) {
            String[] split = claims[i].split(":");
            builder.withClaim(split[0], split[1]);
        }

        builder.withExpiresAt(Instant.ofEpochMilli(System.currentTimeMillis() + expiration));

        return builder.sign(ALGORITHM);
    }

    /**
     * Get the user's ID from a token.
     *
     * @param token The JWT token.
     * @return The user's ID from the token, null if expired or invalid token.
     */
    public static String checkToken(String token) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = VERIFIER.verify(token);
        } catch (JWTVerificationException exception) { // this also handles if the token's expired.
            return null;
        }

        return decodedJWT.getClaim("uuid").asString();
    }

    /**
     * Does what checkToken does but returns boolean like it really ought to do.
     * @param token
     * @return
     */
    public static boolean verifyToken(String token)
    {
        try
        {
            DecodedJWT decodedJWT = VERIFIER.verify(token);
        } catch (JWTVerificationException exception) {
            return false;
        }

        return true;
    }

    public static DecodedJWT decodeJWT(String token)
    {
        try
        {
            return VERIFIER.verify(token);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Algorithm getAlgorithm()
    {
        return ALGORITHM;
    }

    /**
     * Load the HMAC secret key from file.
     *
     * @return The secret HMAC key.
     */
    private static String getHmacKey() {
        File hmacFile = new File("key" + File.separator + "hmac");

        if (!hmacFile.exists())
        {
            hmacFile.getParentFile().mkdirs();
            try
            {
                hmacFile.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            String b64key = null;

            try (FileWriter writer = new FileWriter("key" + File.separator + "hmac")) {
                KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA512");
                SecretKey key = keygen.generateKey();
                b64key = Base64.getEncoder().encodeToString(key.getEncoded());
                writer.write(b64key);
            } catch(IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            LOGGER.warn("HS512 JWT key generated & written to disk");
            return b64key;
        }

        if (!hmacFile.canRead() || !hmacFile.exists()) {
            LOGGER.error("There was an issue loading the HMAC file!");
            System.exit(-1);
        }

        try {
            return new String(Files.readAllBytes(hmacFile.toPath()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER.error("There was an issue loading the HMAC file!");
        System.exit(-1);

        return ""; // this shouldn't ever happen
    }
}

package org.example.msvcauth.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {
    public static final long EXPIRATION_TIME = 864_000_000; // 10 días
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    //public static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final String SECRET_KEY = "MWz43UUUAyIjFjpEoLZvOO4W6BY4qENXZcQcsewuzwM=";

    public static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));


    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
}

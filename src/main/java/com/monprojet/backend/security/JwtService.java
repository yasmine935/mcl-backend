package com.monprojet.backend.security;

import com.monprojet.backend.model.Utilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final SecretKey key;
    private final long expirationMillis;

    public JwtService(@Value("${app.jwt.secret:}") String secret,
                      @Value("${app.jwt.expiration-hours:12}") long expirationHours) {
        if (secret == null || secret.isBlank()) {
            this.key = Jwts.SIG.HS256.key().build();
            log.warn("app.jwt.secret non défini : clé aléatoire générée — les jetons seront invalidés à chaque redémarrage");
        } else {
            // HS256 exige au moins 32 octets de secret
            this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        this.expirationMillis = expirationHours * 3_600_000L;
    }

    public String genererToken(Utilisateur u) {
        Date maintenant = new Date();
        return Jwts.builder()
                .subject(u.getUsername())
                .claim("uid", u.getId())
                .claim("role", u.getRole())
                .issuedAt(maintenant)
                .expiration(new Date(maintenant.getTime() + expirationMillis))
                .signWith(key)
                .compact();
    }

    /** Renvoie les claims si le jeton est valide et non expiré, sinon null. */
    public Claims validerToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}

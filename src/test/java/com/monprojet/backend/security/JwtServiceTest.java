package com.monprojet.backend.security;

import com.monprojet.backend.model.Utilisateur;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET = "cle-de-test-suffisamment-longue-0123456789";

    private final JwtService jwtService = new JwtService(SECRET, 1);

    private Utilisateur utilisateur(Long id, String username, String role) {
        Utilisateur u = new Utilisateur();
        u.setId(id);
        u.setUsername(username);
        u.setRole(role);
        return u;
    }

    @Test
    void unJetonValideRestitueSesClaims() {
        String token = jwtService.genererToken(utilisateur(7L, "jean", "TECHNICIEN"));

        Claims claims = jwtService.validerToken(token);

        assertNotNull(claims);
        assertEquals("jean", claims.getSubject());
        assertEquals(7L, claims.get("uid", Long.class));
        assertEquals("TECHNICIEN", claims.get("role", String.class));
        assertNotNull(claims.getExpiration());
    }

    @Test
    void unJetonExpireEstRefuse() {
        JwtService serviceExpirationNulle = new JwtService(SECRET, 0);
        String token = serviceExpirationNulle.genererToken(utilisateur(1L, "jean", "TECHNICIEN"));

        assertNull(serviceExpirationNulle.validerToken(token));
    }

    @Test
    void unJetonSigneAvecUneAutreCleEstRefuse() {
        JwtService autreService = new JwtService("une-autre-cle-differente-0123456789abcdef", 1);
        String token = autreService.genererToken(utilisateur(1L, "jean", "FERID"));

        assertNull(jwtService.validerToken(token));
    }

    @Test
    void unJetonAltereEstRefuse() {
        String token = jwtService.genererToken(utilisateur(1L, "jean", "TECHNICIEN"));
        String altere = token.substring(0, token.length() - 3) + "abc";

        assertNull(jwtService.validerToken(altere));
    }

    @Test
    void uneChaineQuelconqueEstRefusee() {
        assertNull(jwtService.validerToken("pas-un-jwt"));
        assertNull(jwtService.validerToken(""));
    }
}

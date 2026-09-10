package com.monprojet.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gestion d'erreurs globale : erreurs de validation en 400 lisibles,
 * toute exception imprévue en 500 générique (jamais de stack trace côté client).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex) {
        Map<String, String> champs = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> champs.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of(
                "erreur", "Validation échouée",
                "champs", champs
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> corpsIllisible(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("erreur", "Corps de requête invalide"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> argumentInvalide(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("erreur", ex.getMessage()));
    }

    // Les refus de Spring Security (@PreAuthorize) doivent rester des 403, pas des 500
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> accesRefuse(org.springframework.security.access.AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("erreur", "Accès refusé"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> interne(Exception ex) {
        log.error("Erreur interne non gérée", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("erreur", "Erreur interne du serveur"));
    }
}

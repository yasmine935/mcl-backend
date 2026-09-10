package com.monprojet.backend.config;

import com.monprojet.backend.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${app.cors.allowed-origins:http://localhost:4200}")
    private String[] allowedOrigins;

    // Rôles internes (tous sauf CLIENT) : un compte client externe ne doit atteindre
    // que son espace ticketing, jamais les endpoints métier internes.
    private static final String[] ROLES_INTERNES = {
        "ADMINISTRATEUR", "DIRECTION", "RH", "COMPTABILITE", "SUPPLY_CHAIN",
        "ADMINISTRATIF", "MANAGER", "TECHNICIEN", "TECHNICIEN_SUP"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Dispatch d'erreur Spring : sans ça, toute erreur serveur devient un 401 opaque
                .requestMatchers("/error").permitAll()
                // Authentification
                .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/forgot-password").permitAll()
                // Réservé à l'administrateur
                .requestMatchers("/api/auth/reset-requests/**").hasAuthority("ADMINISTRATEUR")
                // Écrans publics : borne d'accueil visiteurs (lecture seule) et
                // dépôt de ticket (création uniquement). La LISTE des tickets n'est plus
                // publique — elle expose des données personnelles (téléphone, email).
                .requestMatchers(HttpMethod.GET, "/api/visiteurs").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/tickets").permitAll()
                // Chacun peut changer son propre mot de passe (clients inclus)
                .requestMatchers(HttpMethod.PUT, "/api/auth/change-password").authenticated()
                // Espace ticketing client : accessible au client ET aux valideurs internes
                // (le cloisonnement fin est assuré par le service)
                .requestMatchers("/api/tickets-client/**").authenticated()
                // Tout le reste = domaine interne : interdit aux comptes CLIENT
                .anyRequest().hasAnyAuthority(ROLES_INTERNES))
            .exceptionHandling(e -> e.authenticationEntryPoint(
                (request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(allowedOrigins));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

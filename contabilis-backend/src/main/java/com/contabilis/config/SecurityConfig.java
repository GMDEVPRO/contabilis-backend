package com.contabilis.config;

import com.contabilis.security.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Value("${cors.allowed-origins}")
    private String allowedOrigin;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ── Autenticação (público) ──
                        .requestMatchers("/api/auth/**").permitAll()

                        // ── Serviços (público — para o select do agendamento) ──
                        .requestMatchers(HttpMethod.GET, "/api/servicos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/servicos/**").permitAll()

                        // ── Swagger (público) ──
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()

                        // ── Agendamentos ──
                        .requestMatchers(HttpMethod.POST, "/api/agendamentos").authenticated()       // cliente cria
                        .requestMatchers(HttpMethod.GET, "/api/agendamentos/meus").authenticated()   // cliente vê os seus
                        .requestMatchers(HttpMethod.GET, "/api/agendamentos").hasRole("ADMIN")       // admin vê todos
                        .requestMatchers(HttpMethod.GET, "/api/agendamentos/**").authenticated()     // busca por id
                        .requestMatchers(HttpMethod.PUT, "/api/agendamentos/**").hasRole("ADMIN")    // admin atualiza status
                        .requestMatchers(HttpMethod.DELETE, "/api/agendamentos/**").authenticated()  // cancela

                        // ── Pagamentos ──
                        .requestMatchers(HttpMethod.POST, "/api/pagamentos").authenticated()         // cliente paga
                        .requestMatchers(HttpMethod.GET, "/api/pagamentos/meus").authenticated()     // cliente vê os seus
                        .requestMatchers(HttpMethod.GET, "/api/pagamentos").hasRole("ADMIN")         // admin vê todos

                        // ── Admin (rotas exclusivas) ──
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // ── Qualquer outra rota exige autenticação ──
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(allowedOrigin));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
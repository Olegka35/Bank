package com.tarasov.bank.account.contract;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@TestConfiguration
public class FilterChainConfiguration {

    @Bean
    SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                );
        return http.build();
    }

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        return token ->
                Jwt.withTokenValue(token)
                        .header("alg", "none")
                        .subject("oleg")
                        .claim("realm_access", Map.of("roles", List.of("ACCOUNTS_WRITE")))
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plusSeconds(360))
                        .build();
    }
}

package com.tarasov.bank.frontend.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${AUTH_SERVER_URL}")
    private String authServerUrl;

    @Value("${FRONT_UI_URL}")
    private String frontUiUrl;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            String logoutUrl =
                                    String.format("%s/realms/bank/protocol/openid-connect/logout?client_id=%s&post_logout_redirect_uri=%s",
                                            authServerUrl, clientId, frontUiUrl);
                            response.sendRedirect(logoutUrl);
                        })
                );
         return http.build();
    }
}

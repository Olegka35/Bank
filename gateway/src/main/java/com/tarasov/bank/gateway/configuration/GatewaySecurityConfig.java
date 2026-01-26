package com.tarasov.bank.gateway.configuration;

import com.tarasov.bank.gateway.security.JwtTokenRelayGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/actuator/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oath2 -> oath2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public JwtTokenRelayGatewayFilterFactory jwtTokenRelayGatewayFilterFactory() {
        return new JwtTokenRelayGatewayFilterFactory();
    }
}

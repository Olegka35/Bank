package com.tarasov.bank.frontend.security;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@TestConfiguration
public class TestSecurityConfig {

    @Bean(name = "testUserDetailsService")
    public UserDetailsService testUserDetailsService() {
        return username -> {
            if ("oleg".equals(username)) {
                return new CustomUserDetails(
                        "oleg",
                        "test_password",
                        "Oleg Tarasov",
                        List.of(
                                new SimpleGrantedAuthority("ROLE_ACCOUNT_UPDATE"),
                                new SimpleGrantedAuthority("ROLE_CASH_MANAGER"),
                                new SimpleGrantedAuthority("ROLE_MONEY_TRANSFER")
                        )
                );
            }
            throw new UsernameNotFoundException("User not found");
        };
    }
}

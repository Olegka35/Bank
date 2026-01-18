package com.tarasov.bank.account.controller;


import com.tarasov.bank.account.dto.AccountUpdateRequest;
import com.tarasov.bank.account.model.AccountResponse;
import com.tarasov.bank.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Validated
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account")
    @PreAuthorize("isAuthenticated()")
    public AccountResponse getAccountDetails(Authentication authentication) {
        String login = authentication.getName();
        if (!accountService.existsByLogin(login)) { // First authentication via OAuth2
            String fullName = ((Jwt) authentication.getPrincipal()).getClaimAsString("name");
            accountService.createAccount(login, fullName, null, BigDecimal.ZERO);
        }
        return accountService.getAccountInfo(login);
    }

    @PostMapping("/account")
    @PreAuthorize("hasRole('ACCOUNT_UPDATE')")
    public void updateAccount(@RequestBody @Valid AccountUpdateRequest request,
                              Authentication authentication) {
        accountService.updateAccountInfo(authentication.getName(),
                request.name(),
                request.birthdate()
        );
    }
}

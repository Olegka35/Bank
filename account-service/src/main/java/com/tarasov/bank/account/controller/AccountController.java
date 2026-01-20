package com.tarasov.bank.account.controller;


import com.tarasov.bank.account.model.dto.*;
import com.tarasov.bank.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Validated
@RequiredArgsConstructor
public class AccountController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

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

    @PostMapping("/account/{login}/cash")
    @PreAuthorize("hasRole('ACCOUNTS_WRITE')")
    public BalanceResponse updateAccountBalance(@PathVariable String login,
                                                @RequestBody @Valid BalanceUpdateRequest request) {
        return accountService.updateAccountBalance(login, request.action(), request.value());
    }

    @PostMapping("/account/{login}/transfer")
    @PreAuthorize("hasRole('ACCOUNTS_WRITE')")
    public BalanceResponse transferMoney(@PathVariable String login,
                              @RequestBody @Valid MoneyTransferRequest request) {
        return accountService.transferMoney(login, request.login(), request.value());
    }
}

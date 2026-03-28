package com.tarasov.bank.account.controller;


import com.tarasov.bank.account.model.dto.*;
import com.tarasov.bank.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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
        LOGGER.info("Incoming request: GET /account");
        String login = authentication.getName();
        if (!accountService.existsByLogin(login)) { // First authentication via OAuth2
            String fullName = ((Jwt) authentication.getPrincipal()).getClaimAsString("name");
            LOGGER.info("Creating new account: {}", fullName);
            accountService.createAccount(login, fullName, null, BigDecimal.ZERO);
            LOGGER.info("Account created: {}", fullName);
        }
        var accountInfo = accountService.getAccountInfo(login);
        LOGGER.info("Response: {}", accountInfo);
        return accountInfo;
    }

    @PostMapping("/account")
    @PreAuthorize("hasRole('ACCOUNT_UPDATE')")
    public AccountUpdateResponse updateAccount(@RequestBody @Valid AccountUpdateRequest request,
                                               Authentication authentication) {
        LOGGER.info("Incoming request: POST /account: {}", request);
        var response = accountService.updateAccountInfo(authentication.getName(),
                request.name(),
                request.birthdate()
        );
        LOGGER.info("Response: {}", response);
        return response;
    }

    @PostMapping("/account/{login}/cash")
    @PreAuthorize("hasRole('ACCOUNTS_WRITE')")
    public BalanceResponse updateAccountBalance(@PathVariable String login,
                                                @RequestBody @Valid BalanceUpdateRequest request) {
        MDC.put("username", login); // service type auth
        try {
            LOGGER.info("Incoming request: POST /account/{}/case: {}", login, request);
            var response = accountService.updateAccountBalance(login, request.action(), request.value());
            LOGGER.info("Response: {}", response);
            return response;
        } finally {
            MDC.remove("username");
        }
    }

    @PostMapping("/account/{login}/transfer")
    @PreAuthorize("hasRole('ACCOUNTS_WRITE')")
    public BalanceResponse transferMoney(@PathVariable String login,
                                         @RequestBody @Valid MoneyTransferRequest request) {
        MDC.put("username", login); // service type auth
        try {
            LOGGER.info("Incoming request: POST /account/{}/transfer: {}", login, request);
            var response = accountService.transferMoney(login, request.login(), request.value());
            LOGGER.info("Response: {}", response);
            return response;
        } finally {
            MDC.remove("username");
        }
    }
}

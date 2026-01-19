package com.tarasov.bank.cash.controller;


import com.tarasov.bank.cash.dto.BalanceUpdateRequest;
import com.tarasov.bank.cash.service.CashService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import java.math.BigDecimal;

@RestController
@Validated
@RequiredArgsConstructor
public class CashServiceController {

    private final CashService cashService;

    @PostMapping("/cash")
    @PreAuthorize("hasRole('CASH_MANAGER')")
    public BigDecimal updateAccount(@RequestBody @Valid BalanceUpdateRequest request,
                                    Authentication authentication) {
        return cashService.updateBalance(authentication.getName(), request);
    }

    @ExceptionHandler({ HttpStatusCodeException.class })
    public ResponseEntity<String> handleException(HttpStatusCodeException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getMessage());
    }
}

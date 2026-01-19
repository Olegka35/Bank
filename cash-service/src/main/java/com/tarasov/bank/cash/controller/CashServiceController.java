package com.tarasov.bank.cash.controller;


import com.tarasov.bank.cash.dto.BalanceUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class CashServiceController {

    @PostMapping("/cash")
    @PreAuthorize("hasRole('CASH_MANAGER')")
    public String updateAccount(@RequestBody @Valid BalanceUpdateRequest request) {
        return request.toString();
    }
}

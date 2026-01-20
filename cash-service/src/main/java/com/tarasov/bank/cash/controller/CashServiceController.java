package com.tarasov.bank.cash.controller;


import com.tarasov.bank.cash.dto.BalanceResponse;
import com.tarasov.bank.cash.dto.BalanceUpdateRequest;
import com.tarasov.bank.cash.service.CashService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
@RequiredArgsConstructor
public class CashServiceController {

    private final CashService cashService;

    @PostMapping("/cash")
    @PreAuthorize("hasRole('CASH_MANAGER')")
    public BalanceResponse updateAccount(@RequestBody @Valid BalanceUpdateRequest request,
                                         Authentication authentication) {
        return cashService.updateBalance(authentication.getName(), request);
    }
}

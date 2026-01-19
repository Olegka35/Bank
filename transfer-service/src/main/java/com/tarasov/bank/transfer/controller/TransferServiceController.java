package com.tarasov.bank.transfer.controller;


import com.tarasov.bank.transfer.dto.MoneyTransferRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class TransferServiceController {

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('MONEY_TRANSFER')")
    public String updateAccount(@RequestBody @Valid MoneyTransferRequest request) {
        return request.toString();
    }
}

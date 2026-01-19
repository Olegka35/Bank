package com.tarasov.bank.transfer.controller;


import com.tarasov.bank.transfer.dto.MoneyTransferRequest;
import com.tarasov.bank.transfer.service.MoneyTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
public class TransferServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferServiceController.class);

    private final MoneyTransferService moneyTransferService;

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('MONEY_TRANSFER')")
    public void updateAccount(@RequestBody @Valid MoneyTransferRequest request,
                                Authentication authentication) {
        moneyTransferService.transferMoney(authentication.getName(), request);
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error(e.getMessage(), e);
        return e.getMessage();
    }
}

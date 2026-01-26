package com.tarasov.bank.transfer.controller;


import com.tarasov.bank.common.dto.ApiError;
import com.tarasov.bank.transfer.dto.BalanceResponse;
import com.tarasov.bank.transfer.dto.MoneyTransferRequest;
import com.tarasov.bank.transfer.service.MoneyTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Validated
@RequiredArgsConstructor
public class TransferServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferServiceController.class);

    private final MoneyTransferService moneyTransferService;

    @Value("${spring.application.name}")
    private String serviceName;

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('MONEY_TRANSFER')")
    public BalanceResponse updateAccount(@RequestBody @Valid MoneyTransferRequest request,
                                         Authentication authentication) {
        return moneyTransferService.transferMoney(authentication.getName(), request);
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(e.getMessage(), serviceName, LocalDateTime.now()));
    }
}

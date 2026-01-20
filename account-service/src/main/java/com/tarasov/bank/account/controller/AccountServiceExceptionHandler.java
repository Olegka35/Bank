package com.tarasov.bank.account.controller;


import com.tarasov.bank.account.model.exception.AccountNotFoundException;
import com.tarasov.bank.account.model.exception.InsufficientBalanceException;
import com.tarasov.bank.common.dto.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class AccountServiceExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceExceptionHandler.class);

    @Value("${spring.application.name}")
    private String serviceName;

    @ExceptionHandler({ InsufficientBalanceException.class })
    public ResponseEntity<ApiError> handleNotEnoughBalanceError(InsufficientBalanceException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                .body(new ApiError(e.getMessage(), serviceName, LocalDateTime.now()));
    }

    @ExceptionHandler({ AccountNotFoundException.class })
    public ResponseEntity<ApiError> handleAccountNotFoundError(AccountNotFoundException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(e.getMessage(), serviceName, LocalDateTime.now()));
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(e.getMessage(), serviceName, LocalDateTime.now()));
    }
}

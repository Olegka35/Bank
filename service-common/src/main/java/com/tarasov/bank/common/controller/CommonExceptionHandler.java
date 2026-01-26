package com.tarasov.bank.common.controller;

import com.tarasov.bank.common.dto.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDateTime;

@ControllerAdvice
public class CommonExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @Value("${spring.application.name}")
    private String serviceName;

    @ExceptionHandler({ HttpStatusCodeException.class })
    public ResponseEntity<ApiError> handleApiError(HttpStatusCodeException e) {
        ApiError apiError = e.getResponseBodyAs(ApiError.class);
        LOGGER.error("ApiError: {}", apiError);
        return ResponseEntity.status(e.getStatusCode())
                .body(apiError);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ApiError> handleInternalError(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(e.getMessage(), serviceName, LocalDateTime.now()));
    }
}

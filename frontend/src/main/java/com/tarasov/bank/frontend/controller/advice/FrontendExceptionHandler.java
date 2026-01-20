package com.tarasov.bank.frontend.controller.advice;


import com.tarasov.bank.common.dto.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class FrontendExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendExceptionHandler.class);

    @ExceptionHandler( {
            HttpClientErrorException.Unauthorized.class,
            HttpClientErrorException.Forbidden.class
    } )
    public String handleUnauthorizedException(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return printError("Not enough permissions to perform this operation");
    }

    @ExceptionHandler( { HttpStatusCodeException.class } )
    public String handleWebClientException(HttpStatusCodeException ex) {
        ApiError apiError = ex.getResponseBodyAs(ApiError.class);
        if (apiError == null) {
            LOGGER.error(ex.getMessage(), ex);
            return printError("Internal error occurred. Please contact administrator.");
        }
        LOGGER.error("ApiError: {}", apiError);
        return printError(apiError.message());
    }

    @ExceptionHandler( { MethodArgumentNotValidException.class } )
    public String handleIncorrectParameterException(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return printError("Incorrect request parameters");
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return printError("Internal error occurred. Please contact administrator.");
    }

    private String printError(String error) {
        return "redirect:/?errors=" + URLEncoder.encode(error, StandardCharsets.UTF_8);
    }
}

package com.tarasov.bank.frontend.controller.advice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler( {
            HttpClientErrorException.Unauthorized.class,
            HttpClientErrorException.Forbidden.class
    } )
    public String handleUnauthorizedException(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return printError("Not enough permissions to perform this operation");
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
        return "redirect:/?errors=" + error;
    }
}

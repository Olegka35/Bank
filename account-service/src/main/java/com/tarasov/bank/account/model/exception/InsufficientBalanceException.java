package com.tarasov.bank.account.model.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Not enough balance to perform this operation");
    }
}

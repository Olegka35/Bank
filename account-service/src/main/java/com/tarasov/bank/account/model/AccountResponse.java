package com.tarasov.bank.account.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountResponse(
        String name,
        LocalDate birthdate,
        BigDecimal sum
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getFullName(), account.getBirthdate(), account.getBalance());
    }
}

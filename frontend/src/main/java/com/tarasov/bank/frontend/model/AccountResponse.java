package com.tarasov.bank.frontend.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AccountResponse(
        String name,
        LocalDate birthdate,
        BigDecimal sum,
        List<Account> accounts
) {
}

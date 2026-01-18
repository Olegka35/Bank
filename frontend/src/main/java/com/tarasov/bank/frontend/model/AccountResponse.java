package com.tarasov.bank.frontend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountResponse(
        String name,
        LocalDate birthdate,
        BigDecimal sum
) {
}

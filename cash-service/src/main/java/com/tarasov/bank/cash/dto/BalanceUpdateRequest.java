package com.tarasov.bank.cash.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BalanceUpdateRequest(
        @Positive @Max(1_000_000) BigDecimal value,
        Action action
) {
}

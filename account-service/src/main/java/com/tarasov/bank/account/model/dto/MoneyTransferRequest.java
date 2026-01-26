package com.tarasov.bank.account.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MoneyTransferRequest(
        @Positive @Max(1_000_000) BigDecimal value,
        @NotBlank @Size(max = 50) String login
) {
}

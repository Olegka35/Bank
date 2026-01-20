package com.tarasov.bank.cash.dto;

import java.math.BigDecimal;

public record BalanceResponse(
        BigDecimal balance
) {
}

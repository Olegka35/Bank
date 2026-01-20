package com.tarasov.bank.account.model.dto;

import java.math.BigDecimal;

public record BalanceResponse(
        BigDecimal balance
) {
}

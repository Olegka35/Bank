package com.tarasov.bank.transfer.dto;

import java.math.BigDecimal;

public record BalanceResponse(
        BigDecimal balance
) {
}

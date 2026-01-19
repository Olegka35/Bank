package com.tarasov.bank.cash.service;

import com.tarasov.bank.cash.dto.BalanceUpdateRequest;

import java.math.BigDecimal;

public interface CashService {

    BigDecimal updateBalance(String login, BalanceUpdateRequest balanceUpdateRequest);
}

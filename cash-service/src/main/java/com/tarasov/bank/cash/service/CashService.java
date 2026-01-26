package com.tarasov.bank.cash.service;

import com.tarasov.bank.cash.dto.BalanceResponse;
import com.tarasov.bank.cash.dto.BalanceUpdateRequest;


public interface CashService {

    BalanceResponse updateBalance(String login, BalanceUpdateRequest balanceUpdateRequest);
}

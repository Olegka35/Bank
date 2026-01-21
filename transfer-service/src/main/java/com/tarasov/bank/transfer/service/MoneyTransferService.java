package com.tarasov.bank.transfer.service;

import com.tarasov.bank.transfer.dto.BalanceResponse;
import com.tarasov.bank.transfer.dto.MoneyTransferRequest;

public interface MoneyTransferService {
    BalanceResponse transferMoney(String login, MoneyTransferRequest moneyTransferRequest);
}

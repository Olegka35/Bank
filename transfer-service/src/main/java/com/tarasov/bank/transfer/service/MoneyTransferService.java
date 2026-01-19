package com.tarasov.bank.transfer.service;

import com.tarasov.bank.transfer.dto.MoneyTransferRequest;

public interface MoneyTransferService {
    void transferMoney(String login, MoneyTransferRequest moneyTransferRequest);
}

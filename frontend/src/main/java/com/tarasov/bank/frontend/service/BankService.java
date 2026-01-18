package com.tarasov.bank.frontend.service;

import com.tarasov.bank.frontend.model.AccountResponse;
import com.tarasov.bank.frontend.model.AccountUpdateRequest;
import com.tarasov.bank.frontend.model.BalanceUpdateRequest;
import com.tarasov.bank.frontend.model.MoneyTransferRequest;


public interface BankService {
    AccountResponse getAccountDetails();
    void updateAccount(AccountUpdateRequest request);
    void updateBalance(BalanceUpdateRequest request);
    void transferMoney(MoneyTransferRequest request);
}

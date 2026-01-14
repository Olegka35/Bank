package com.tarasov.bank.frontend.service;

import com.tarasov.bank.frontend.model.AccountDto;
import com.tarasov.bank.frontend.model.AccountUpdateRequest;
import com.tarasov.bank.frontend.model.BalanceUpdateRequest;
import com.tarasov.bank.frontend.model.MoneyTransferRequest;


public interface BankService {
    AccountDto getAccountDetails();
    void updateAccount(AccountUpdateRequest request);
    void updateBalance(BalanceUpdateRequest request);
    void transferMoney(MoneyTransferRequest request);
}

package com.tarasov.bank.account.service;

import com.tarasov.bank.account.model.dto.AccountUpdateResponse;
import com.tarasov.bank.account.model.dto.Action;
import com.tarasov.bank.account.model.Account;
import com.tarasov.bank.account.model.dto.AccountResponse;
import com.tarasov.bank.account.model.dto.BalanceResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AccountService {

    boolean existsByLogin(String login);
    Account createAccount(String login, String fullName, LocalDate birthday, BigDecimal balance);
    AccountResponse getAccountInfo(String login);
    AccountUpdateResponse updateAccountInfo(String login, String fullName, LocalDate birthdate);
    BalanceResponse updateAccountBalance(String login, Action action, BigDecimal amount);
    BalanceResponse transferMoney(String senderLogin, String recipientLogin, BigDecimal amount);
}

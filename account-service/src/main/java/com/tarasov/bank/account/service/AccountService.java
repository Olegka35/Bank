package com.tarasov.bank.account.service;

import com.tarasov.bank.account.dto.Action;
import com.tarasov.bank.account.model.Account;
import com.tarasov.bank.account.model.AccountResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AccountService {

    boolean existsByLogin(String login);
    Account createAccount(String login, String fullName, LocalDate birthday, BigDecimal balance);
    AccountResponse getAccountInfo(String login);
    void updateAccountInfo(String login, String fullName, LocalDate birthdate);
    BigDecimal updateAccountBalance(String login, Action action, BigDecimal amount);
    BigDecimal transferMoney(String senderLogin, String recipientLogin, BigDecimal amount);
}

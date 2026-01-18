package com.tarasov.bank.account.service;

import com.tarasov.bank.account.model.Account;
import com.tarasov.bank.account.model.AccountResponse;
import com.tarasov.bank.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public boolean existsByLogin(String login) {
        return accountRepository.existsByLogin(login);
    }

    @Override
    @Transactional
    public Account createAccount(String login, String fullName, LocalDate birthday, BigDecimal balance) {
        Account account = new Account(login, fullName, birthday, balance);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public AccountResponse getAccountInfo(String login) {
        return AccountResponse.from(accountRepository.findByLogin(login));
    }

    @Override
    @Transactional
    public void updateAccountInfo(String login, String fullName, LocalDate birthdate) {
        Account account = accountRepository.findByLogin(login);
        if (account == null) {
            throw new NoSuchElementException("Account not found");
        }
        account.setFullName(fullName);
        account.setBirthdate(birthdate);
        accountRepository.save(account);
    }
}

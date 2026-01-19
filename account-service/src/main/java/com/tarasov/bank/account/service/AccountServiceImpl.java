package com.tarasov.bank.account.service;

import com.tarasov.bank.account.client.NotificationServiceRestClient;
import com.tarasov.bank.account.dto.Action;
import com.tarasov.bank.account.dto.NotificationRequest;
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
    private final NotificationServiceRestClient notificationServiceRestClient;

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

        String notificationMessage =
                String.format("Account details updated: full name -> %s, birth date -> %s",
                        fullName, birthdate);
        notificationServiceRestClient
                .post("/notify")
                .body(new NotificationRequest(login, notificationMessage))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    @Transactional
    public BigDecimal updateAccountBalance(String login, Action action, BigDecimal amount) {
        Account account = accountRepository.findByLogin(login);
        if (account == null) {
            throw new NoSuchElementException("Account not found");
        }
        if (Action.GET.equals(action)) {
            if (account.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Not enough balance");
            }
            account.setBalance(account.getBalance().subtract(amount));
        } else if (Action.PUT.equals(action)) {
            account.setBalance(account.getBalance().add(amount));
        }
        account = accountRepository.save(account);
        return account.getBalance();
    }

    @Override
    @Transactional
    public BigDecimal transferMoney(String senderLogin, String recipientLogin, BigDecimal amount) {
        if (senderLogin.equals(recipientLogin)) {
            throw new IllegalArgumentException("Not possible to transfer money to yourselves");
        }
        Account sender = accountRepository.findByLogin(senderLogin);
        Account recipient = accountRepository.findByLogin(recipientLogin);
        if (sender == null || recipient == null) {
            throw new NoSuchElementException("Account not found");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Not enough balance");
        }
        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));
        sender = accountRepository.save(sender);
        accountRepository.save(recipient);
        return sender.getBalance();
    }
}

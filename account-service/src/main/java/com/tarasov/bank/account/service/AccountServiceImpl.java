package com.tarasov.bank.account.service;

import com.tarasov.bank.account.client.NotificationServiceClient;
import com.tarasov.bank.account.model.dto.*;
import com.tarasov.bank.account.model.Account;
import com.tarasov.bank.account.model.exception.AccountNotFoundException;
import com.tarasov.bank.account.model.exception.InsufficientBalanceException;
import com.tarasov.bank.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final NotificationServiceClient notificationServiceClient;

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
        Account account = accountRepository.findByLogin(login);
        List<Account> otherAccounts = accountRepository.findAccountsByLoginNot(login);
        return AccountResponse.from(account, otherAccounts);
    }

    @Override
    @Transactional
    public AccountUpdateResponse updateAccountInfo(String login, String fullName, LocalDate birthdate) {
        Account account = accountRepository.findByLogin(login);
        if (account == null) {
            throw new AccountNotFoundException();
        }
        account.setFullName(fullName);
        account.setBirthdate(birthdate);
        account = accountRepository.save(account);

        String notificationMessage =
                String.format("Account details updated: full name -> %s, birth date -> %s",
                        fullName, birthdate);
        notificationServiceClient.send(new NotificationRequest(login, notificationMessage));
        return new AccountUpdateResponse(account.getLogin(), account.getFullName(), account.getBirthdate());
    }

    @Override
    @Transactional
    public BalanceResponse updateAccountBalance(String login, Action action, BigDecimal amount) {
        Account account = accountRepository.findByLogin(login);
        if (account == null) {
            throw new AccountNotFoundException();
        }
        if (Action.GET.equals(action)) {
            if (account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientBalanceException();
            }
            account.setBalance(account.getBalance().subtract(amount));
        } else if (Action.PUT.equals(action)) {
            account.setBalance(account.getBalance().add(amount));
        }
        account = accountRepository.save(account);
        return new BalanceResponse(account.getBalance());
    }

    @Override
    @Transactional
    public BalanceResponse transferMoney(String senderLogin, String recipientLogin, BigDecimal amount) {
        if (senderLogin.equals(recipientLogin)) {
            throw new IllegalArgumentException("Not possible to transfer money to yourselves");
        }
        Account sender = accountRepository.findByLogin(senderLogin);
        Account recipient = accountRepository.findByLogin(recipientLogin);
        if (sender == null || recipient == null) {
            throw new AccountNotFoundException();
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }
        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));
        sender = accountRepository.save(sender);
        accountRepository.save(recipient);
        return new BalanceResponse(sender.getBalance());
    }
}

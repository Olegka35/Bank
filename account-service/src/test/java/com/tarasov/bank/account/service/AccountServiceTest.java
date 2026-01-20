package com.tarasov.bank.account.service;


import com.tarasov.bank.account.client.NotificationServiceClient;
import com.tarasov.bank.account.model.dto.AccountResponse;
import com.tarasov.bank.account.model.dto.Action;
import com.tarasov.bank.account.model.dto.NotificationRequest;
import com.tarasov.bank.account.model.Account;
import com.tarasov.bank.account.model.exception.AccountNotFoundException;
import com.tarasov.bank.account.model.exception.InsufficientBalanceException;
import com.tarasov.bank.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private NotificationServiceClient notificationServiceClient;

    @Test
    public void canDetectAccountExistence() {
        String login = "Oleg";
        when(accountRepository.existsByLogin(login)).thenReturn(Boolean.TRUE);
        assertTrue(accountService.existsByLogin(login));
    }

    @Test
    public void canDetectAccountAbsence() {
        String login = "Oleg";
        when(accountRepository.existsByLogin(login)).thenReturn(Boolean.FALSE);
        assertFalse(accountService.existsByLogin(login));
        verify(accountRepository).existsByLogin(login);
    }

    @Test
    public void canCreateAccount() {
        Account testAccount = new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.ZERO);
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        assertEquals(testAccount, accountService.createAccount("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.ZERO));

        verify(accountRepository).save(testAccount);
    }

    @Test
    public void canGetAccountInfo() {
        Account testAccount = new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(500_000));
        when(accountRepository.findByLogin("Oleg")).thenReturn(testAccount);
        when(accountRepository.findAccountsByLoginNot("Oleg"))
                .thenReturn(List.of(
                        new Account("Test", "Test Account", LocalDate.of(2000, 1, 1), BigDecimal.valueOf(500)),
                        new Account("Trakand", "Venera Trakand", LocalDate.of(2000, 5, 1), BigDecimal.valueOf(500))
                ));

        AccountResponse response = accountService.getAccountInfo("Oleg");

        assertEquals("Oleg Tarasov", response.name());
        assertEquals(BigDecimal.valueOf(500_000), response.sum());
        assertEquals(LocalDate.of(1997, 3, 23), response.birthdate());
        assertEquals(2, response.accounts().size());
        assertEquals("Test", response.accounts().get(0).login());
        assertEquals("Test Account", response.accounts().get(0).name());
        assertEquals("Trakand", response.accounts().get(1).login());
        assertEquals("Venera Trakand", response.accounts().get(1).name());

        verify(accountRepository).findByLogin("Oleg");
        verify(accountRepository).findAccountsByLoginNot("Oleg");
    }

    @Test
    public void updateAccountInfoTest() {
        Account testAccount = new Account("Oleg", "Oleg", null, BigDecimal.valueOf(500_000));
        Account updatedAccount = new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(500_000));
        when(accountRepository.findByLogin("Oleg")).thenReturn(testAccount);
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        accountService.updateAccountInfo("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23));

        verify(accountRepository).save(updatedAccount);
        String notificationMessage =
                String.format("Account details updated: full name -> Oleg Tarasov, birth date -> %s", LocalDate.of(1997, 3, 23));
        verify(notificationServiceClient).send(new NotificationRequest("Oleg", notificationMessage));
    }

    @Test
    public void updateAccountInfoTest_accountNotExists() {
        when(accountRepository.findByLogin("Oleg")).thenReturn(null);

        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.updateAccountInfo("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23))
        );
    }

    @Test
    public void depositMoneyTest() {
        Account account = new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(500_000));
        when(accountRepository.findByLogin("Oleg")).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        BigDecimal newBalance = accountService.updateAccountBalance("Oleg", Action.PUT, BigDecimal.valueOf(1000));

        assertEquals(BigDecimal.valueOf(501_000), newBalance);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());
        assertEquals(new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(501_000)),
                accountArgumentCaptor.getValue());
    }

    @Test
    public void withdrawMoneyTest() {
        Account account = new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(500_000));
        when(accountRepository.findByLogin("Oleg")).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        BigDecimal newBalance = accountService.updateAccountBalance("Oleg", Action.GET, BigDecimal.valueOf(1000));

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());
        assertEquals(new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(499_000)),
                accountArgumentCaptor.getValue());
    }

    @Test
    public void withdrawExtraMoneyTest() {
        Account account = new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(500_000));
        when(accountRepository.findByLogin("Oleg")).thenReturn(account);

        assertThrows(
                InsufficientBalanceException.class,
                () -> accountService.updateAccountBalance("Oleg", Action.GET, BigDecimal.valueOf(1_000_000))
        );
    }

    @ParameterizedTest
    @CsvSource({"GET", "PUT"})
    public void updateTest_notExistingAccount(Action action) {
        when(accountRepository.findByLogin("Oleg")).thenReturn(null);

        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.updateAccountBalance("Oleg", action, BigDecimal.valueOf(1_000_000))
        );
    }

    @Test
    public void transferMoneyTest() {
        Account sender = new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(500_000));
        Account recipient = new Account("Trakand", "Venera Trakand", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(500_000));
        when(accountRepository.findByLogin("Oleg")).thenReturn(sender);
        when(accountRepository.findByLogin("Trakand")).thenReturn(recipient);
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        BigDecimal newBalance = accountService.transferMoney("Oleg", "Trakand", BigDecimal.valueOf(100_000));

        assertEquals(BigDecimal.valueOf(400_000), newBalance);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(2)).save(accountArgumentCaptor.capture());
        assertTrue(accountArgumentCaptor.getAllValues().contains(
                new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(400_000))));
        assertTrue(accountArgumentCaptor.getAllValues().contains(
                new Account("Trakand", "Venera Trakand", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(600_000))));
    }

    @Test
    public void transferMoneyTest_insufficientBalance() {
        Account sender = new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(50_000));
        Account recipient = new Account("Trakand", "Venera Trakand", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(500_000));
        when(accountRepository.findByLogin("Oleg")).thenReturn(sender);
        when(accountRepository.findByLogin("Trakand")).thenReturn(recipient);

        assertThrows(
                InsufficientBalanceException.class,
                () -> accountService.transferMoney("Oleg", "Trakand", BigDecimal.valueOf(100_000))
        );
    }

    @Test
    public void transferMoneyTest_selfTransferProhibition() {
        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.transferMoney("Oleg", "Oleg", BigDecimal.valueOf(100_000))
        );
    }

    @Test
    public void transferMoneyTest_senderNotFound() {
        Account recipient = new Account("Trakand", "Venera Trakand", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(500_000));
        when(accountRepository.findByLogin("Oleg")).thenReturn(null);
        when(accountRepository.findByLogin("Trakand")).thenReturn(recipient);

        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.transferMoney("Oleg", "Trakand", BigDecimal.valueOf(100_000))
        );
    }

    @Test
    public void transferMoneyTest_recipientNotFound() {
        Account sender = new Account("Oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(50_000));
        when(accountRepository.findByLogin("Oleg")).thenReturn(sender);
        when(accountRepository.findByLogin("Trakand")).thenReturn(null);

        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.transferMoney("Oleg", "Trakand", BigDecimal.valueOf(100_000))
        );
    }
}

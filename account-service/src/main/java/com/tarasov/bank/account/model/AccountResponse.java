package com.tarasov.bank.account.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AccountResponse(
        String name,
        LocalDate birthdate,
        BigDecimal sum,
        List<AccountDto> accounts
) {
    public static AccountResponse from(Account account, List<Account> otherAccounts) {
        return new AccountResponse(account.getFullName(),
                account.getBirthdate(),
                account.getBalance(),
                otherAccounts.stream()
                        .map(a -> new AccountDto(a.getLogin(), a.getFullName()))
                        .toList()
        );
    }
}

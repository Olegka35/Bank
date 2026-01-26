package com.tarasov.bank.account.model.dto;

import java.time.LocalDate;

public record AccountUpdateResponse(
        String login,
        String fullName,
        LocalDate birthdate
) {
}

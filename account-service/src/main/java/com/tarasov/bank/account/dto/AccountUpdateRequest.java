package com.tarasov.bank.account.dto;

import com.tarasov.bank.account.dto.validator.Adult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AccountUpdateRequest(
        @NotBlank @Size(max = 50) String name,
        @Adult LocalDate birthdate
) {
}

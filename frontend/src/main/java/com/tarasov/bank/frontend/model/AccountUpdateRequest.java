package com.tarasov.bank.frontend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AccountUpdateRequest(
        @NotBlank @Size(max = 50) String name,
        LocalDate birthdate
) {
}

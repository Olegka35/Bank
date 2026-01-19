package com.tarasov.bank.notification.dto;

import jakarta.validation.constraints.NotBlank;

public record NotificationRequest(
        @NotBlank String message
) {
}

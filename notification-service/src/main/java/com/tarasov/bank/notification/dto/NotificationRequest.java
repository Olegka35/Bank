package com.tarasov.bank.notification.dto;

import jakarta.validation.constraints.NotBlank;

public record NotificationRequest(
        @NotBlank String login,
        @NotBlank String message
) {
}

package com.tarasov.bank.account.model.dto;


public record NotificationRequest(
        String login,
        String message
) {
}

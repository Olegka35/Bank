package com.tarasov.bank.account.dto;


public record NotificationRequest(
        String login,
        String message
) {
}

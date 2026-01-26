package com.tarasov.bank.cash.dto;


public record NotificationRequest(
        String login,
        String message
) {
}

package com.tarasov.bank.transfer.dto;


public record NotificationRequest(
        String login,
        String message
) {
}

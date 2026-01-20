package com.tarasov.bank.transfer.service;

import com.tarasov.bank.transfer.client.AccountServiceClient;
import com.tarasov.bank.transfer.client.NotificationServiceClient;
import com.tarasov.bank.transfer.dto.MoneyTransferRequest;
import com.tarasov.bank.transfer.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final AccountServiceClient accountServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    @Override
    public void transferMoney(String login, MoneyTransferRequest moneyTransferRequest) {
        if (login.equals(moneyTransferRequest.login())) {
            throw new IllegalArgumentException("Not possible to transfer money to yourselves");
        }
        String uri = String.format("/account/%s/transfer", login);
        BigDecimal balance = accountServiceClient.post(uri, moneyTransferRequest, BigDecimal.class);

        String notificationMessage =
                String.format("Money transfer (%.2f р.) -> %s (Remaining balance: %.2f р.)",
                        moneyTransferRequest.value(),
                        moneyTransferRequest.login(),
                        balance);
        notificationServiceClient.send(new NotificationRequest(login, notificationMessage));
    }
}

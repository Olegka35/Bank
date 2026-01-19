package com.tarasov.bank.transfer.service;

import com.tarasov.bank.transfer.client.AccountServiceRestClient;
import com.tarasov.bank.transfer.client.NotificationServiceRestClient;
import com.tarasov.bank.transfer.dto.MoneyTransferRequest;
import com.tarasov.bank.transfer.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final AccountServiceRestClient accountServiceRestClient;
    private final NotificationServiceRestClient notificationServiceRestClient;

    @Override
    public void transferMoney(String login, MoneyTransferRequest moneyTransferRequest) {
        if (login.equals(moneyTransferRequest.login())) {
            throw new IllegalArgumentException("Not possible to transfer money to yourselves");
        }

        BigDecimal balance = accountServiceRestClient.post("/account/" + login + "/transfer")
                .body(moneyTransferRequest)
                .retrieve()
                .body(BigDecimal.class);

        String notificationMessage =
                String.format("Money transfer (%.2f р.) -> %s (Remaining balance: %.2f р.)",
                        moneyTransferRequest.value(),
                        moneyTransferRequest.login(),
                        balance);
        notificationServiceRestClient.post("/notify")
                .body(new NotificationRequest(login, notificationMessage))
                .retrieve()
                .toBodilessEntity();
    }
}

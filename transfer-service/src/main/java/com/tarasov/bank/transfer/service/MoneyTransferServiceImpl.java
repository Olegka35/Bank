package com.tarasov.bank.transfer.service;

import com.tarasov.bank.transfer.client.AccountServiceClient;
import com.tarasov.bank.transfer.dto.BalanceResponse;
import com.tarasov.bank.transfer.dto.MoneyTransferRequest;
import com.tarasov.bank.transfer.dto.NotificationRequest;
import com.tarasov.bank.transfer.producer.KafkaNotificationProducer;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final AccountServiceClient accountServiceClient;
    private final KafkaNotificationProducer notificationProducer;
    private final MeterRegistry meterRegistry;

    @Override
    public BalanceResponse transferMoney(String login, MoneyTransferRequest moneyTransferRequest) {
        if (login.equals(moneyTransferRequest.login())) {
            throw new IllegalArgumentException("Not possible to transfer money to yourselves");
        }
        String uri = String.format("/account/%s/transfer", login);
        BalanceResponse balanceResponse =
                accountServiceClient.post(uri, moneyTransferRequest, BalanceResponse.class);

        try {
            String notificationMessage =
                    String.format("Money transfer (%.2f р.) -> %s (Remaining balance: %.2f р.)",
                            moneyTransferRequest.value(),
                            moneyTransferRequest.login(),
                            balanceResponse.balance());
            notificationProducer.sendNotification(new NotificationRequest(login, notificationMessage));
        } catch (Exception e) {
            meterRegistry.counter("error_notification", "login", login).increment();
            throw e;
        }
        return balanceResponse;
    }
}

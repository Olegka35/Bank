package com.tarasov.bank.cash.service;

import com.tarasov.bank.cash.client.AccountServiceClient;
import com.tarasov.bank.cash.dto.Action;
import com.tarasov.bank.cash.dto.BalanceResponse;
import com.tarasov.bank.cash.dto.BalanceUpdateRequest;
import com.tarasov.bank.cash.dto.NotificationRequest;
import com.tarasov.bank.cash.producer.KafkaNotificationProducer;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CashServiceImpl implements CashService {

    private final AccountServiceClient accountServiceClient;
    private final KafkaNotificationProducer notificationProducer;
    private final MeterRegistry meterRegistry;

    @Override
    public BalanceResponse updateBalance(String login, BalanceUpdateRequest balanceUpdateRequest) {
        String uri = String.format("/account/%s/cash", login);
        BalanceResponse balanceResponse =
                accountServiceClient.post(uri, balanceUpdateRequest, BalanceResponse.class);

        try {
            String notificationMessage =
                    String.format("Balance updated (%s%.2f р.): %.2f р.",
                            balanceUpdateRequest.action().equals(Action.GET) ? "-" : "+",
                            balanceUpdateRequest.value(),
                            balanceResponse.balance());
            notificationProducer.sendNotification(new NotificationRequest(login, notificationMessage));
        } catch (Exception e) {
            meterRegistry.counter("error_notification", "login", login).increment();
            throw e;
        }
        return balanceResponse;
    }
}

package com.tarasov.bank.cash.service;

import com.tarasov.bank.cash.client.AccountServiceRestClient;
import com.tarasov.bank.cash.client.NotificationServiceClient;
import com.tarasov.bank.cash.dto.Action;
import com.tarasov.bank.cash.dto.BalanceUpdateRequest;
import com.tarasov.bank.cash.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CashServiceImpl implements CashService {

    private final AccountServiceRestClient accountServiceRestClient;
    private final NotificationServiceClient notificationServiceClient;

    @Override
    public BigDecimal updateBalance(String login, BalanceUpdateRequest balanceUpdateRequest) {
        BigDecimal balance = accountServiceRestClient.post("/account/" + login + "/cash")
                .body(balanceUpdateRequest)
                .retrieve()
                .body(BigDecimal.class);

        String notificationMessage =
                String.format("Balance updated (%s%.2f р.): %.2f р.",
                        balanceUpdateRequest.action().equals(Action.GET) ? "-" : "+",
                        balanceUpdateRequest.value(),
                        balance);
        notificationServiceClient.send(new NotificationRequest(login, notificationMessage));
        return balance;
    }
}

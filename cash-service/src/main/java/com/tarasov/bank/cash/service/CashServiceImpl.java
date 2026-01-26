package com.tarasov.bank.cash.service;

import com.tarasov.bank.cash.client.AccountServiceClient;
import com.tarasov.bank.cash.client.NotificationServiceClient;
import com.tarasov.bank.cash.dto.Action;
import com.tarasov.bank.cash.dto.BalanceResponse;
import com.tarasov.bank.cash.dto.BalanceUpdateRequest;
import com.tarasov.bank.cash.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CashServiceImpl implements CashService {

    private final AccountServiceClient accountServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    @Override
    public BalanceResponse updateBalance(String login, BalanceUpdateRequest balanceUpdateRequest) {
        String uri = String.format("/account/%s/cash", login);
        BalanceResponse balanceResponse =
                accountServiceClient.post(uri, balanceUpdateRequest, BalanceResponse.class);

        String notificationMessage =
                String.format("Balance updated (%s%.2f р.): %.2f р.",
                        balanceUpdateRequest.action().equals(Action.GET) ? "-" : "+",
                        balanceUpdateRequest.value(),
                        balanceResponse.balance());
        notificationServiceClient.send(new NotificationRequest(login, notificationMessage));
        return balanceResponse;
    }
}

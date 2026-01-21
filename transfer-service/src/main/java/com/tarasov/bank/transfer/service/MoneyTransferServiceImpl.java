package com.tarasov.bank.transfer.service;

import com.tarasov.bank.transfer.client.AccountServiceClient;
import com.tarasov.bank.transfer.client.NotificationServiceClient;
import com.tarasov.bank.transfer.dto.BalanceResponse;
import com.tarasov.bank.transfer.dto.MoneyTransferRequest;
import com.tarasov.bank.transfer.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final AccountServiceClient accountServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    @Override
    public BalanceResponse transferMoney(String login, MoneyTransferRequest moneyTransferRequest) {
        if (login.equals(moneyTransferRequest.login())) {
            throw new IllegalArgumentException("Not possible to transfer money to yourselves");
        }
        String uri = String.format("/account/%s/transfer", login);
        BalanceResponse balanceResponse =
                accountServiceClient.post(uri, moneyTransferRequest, BalanceResponse.class);

        String notificationMessage =
                String.format("Money transfer (%.2f р.) -> %s (Remaining balance: %.2f р.)",
                        moneyTransferRequest.value(),
                        moneyTransferRequest.login(),
                        balanceResponse.balance());
        notificationServiceClient.send(new NotificationRequest(login, notificationMessage));
        return balanceResponse;
    }
}

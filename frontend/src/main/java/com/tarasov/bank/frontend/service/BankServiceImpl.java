package com.tarasov.bank.frontend.service;

import com.tarasov.bank.frontend.client.ApiGatewayClient;
import com.tarasov.bank.frontend.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final ApiGatewayClient apiGatewayClient;

    @Override
    public AccountDto getAccountDetails() {
        return null;
    }

    @Override
    public void updateAccount(AccountUpdateRequest request) {
        String response = apiGatewayClient.post("/account")
                .body(request)
                .retrieve()
                .body(String.class);
        System.out.println(response);
    }

    @Override
    public void updateBalance(BalanceUpdateRequest request) {
        apiGatewayClient.post("/cash")
                .body(request)
                .retrieve()
                .body(String.class);
    }

    @Override
    public void transferMoney(MoneyTransferRequest request) {
        apiGatewayClient.post("/transfer")
                .body(request)
                .retrieve()
                .body(String.class);
    }
}

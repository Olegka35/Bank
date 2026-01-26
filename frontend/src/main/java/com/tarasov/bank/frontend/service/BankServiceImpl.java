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
    public AccountResponse getAccountDetails() {
        return apiGatewayClient.get("/account")
                .retrieve()
                .body(AccountResponse.class);
    }

    @Override
    public void updateAccount(AccountUpdateRequest request) {
        apiGatewayClient.post("/account")
                .body(request)
                .retrieve()
                .toBodilessEntity();
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

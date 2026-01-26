package com.tarasov.bank.cash.client;

import com.tarasov.bank.common.client.AbstractServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AccountServiceClient extends AbstractServiceClient {

    public AccountServiceClient(@Value("${account-service.url}") String accountServiceUrl,
                                @Value("${spring.application.name}") String serviceRegistrationId) {
        super(accountServiceUrl, serviceRegistrationId);
    }
}

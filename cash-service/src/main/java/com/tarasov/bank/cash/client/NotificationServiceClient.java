package com.tarasov.bank.cash.client;


import com.tarasov.bank.cash.dto.NotificationRequest;
import com.tarasov.bank.common.client.AbstractServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class NotificationServiceClient extends AbstractServiceClient {

    public NotificationServiceClient(@Value("${notification-service.url}") String notificationServiceUrl,
                                     @Value("${spring.application.name}") String serviceRegistrationId) {
        super(notificationServiceUrl, serviceRegistrationId);
    }

    public void send(NotificationRequest notificationRequest) {
        super.post("/notify", notificationRequest);
    }
}

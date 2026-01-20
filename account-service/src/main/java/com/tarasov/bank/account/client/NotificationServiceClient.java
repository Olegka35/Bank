package com.tarasov.bank.account.client;


import com.tarasov.bank.account.model.dto.NotificationRequest;
import com.tarasov.bank.common.client.AbstractServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;


@Component
public class NotificationServiceClient extends AbstractServiceClient {

    public NotificationServiceClient(@Value("${notification-service.url}") String notificationServiceUrl,
                                     @Value("${spring.application.name}") String serviceRegistrationId,
                                     OAuth2AuthorizedClientManager authorizedClientManager) {
        super(notificationServiceUrl, serviceRegistrationId, authorizedClientManager);
    }

    public void send(NotificationRequest notificationRequest) {
        super.post("/notify", notificationRequest);
    }
}

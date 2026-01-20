package com.tarasov.bank.account.client;


import com.tarasov.bank.account.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class NotificationServiceClient {

    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final RestClient restClient;
    private final String serviceRegistrationId;

    public NotificationServiceClient(@Value("${notification-service.url}") String notificationServiceUrl,
                                     @Value("${spring.application.name}") String serviceRegistrationId,
                                     OAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
        this.serviceRegistrationId = serviceRegistrationId;
        this.restClient = RestClient.builder()
                .baseUrl(notificationServiceUrl)
                .build();
    }

    public void send(NotificationRequest notificationRequest) {
        String authToken = getAuthToken();
        restClient.post()
                .uri("/notify")
                .headers(h -> h.setBearerAuth(authToken))
                .body(notificationRequest)
                .retrieve()
                .toBodilessEntity();
    }

    private String getAuthToken() {
        return authorizedClientManager.authorize(
                OAuth2AuthorizeRequest
                        .withClientRegistrationId(serviceRegistrationId)
                        .principal("system")
                        .build()
                )
                .getAccessToken()
                .getTokenValue();
    }
}

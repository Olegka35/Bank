package com.tarasov.bank.common.client;

import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

public class AbstractServiceClient {

    private final RestClient restClient;
    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final String serviceRegistrationId;

    public AbstractServiceClient(String serviceUrl,
                                 String serviceRegistrationId,
                                 OAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
        this.serviceRegistrationId = serviceRegistrationId;
        this.restClient = RestClient.builder()
                .baseUrl(serviceUrl)
                .build();
    }

    protected void post(String uri, Object body) {
        String authToken = getAuthToken();
        restClient.post()
                .uri(uri)
                .headers(h -> h.setBearerAuth(authToken))
                .body(body)
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

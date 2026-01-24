package com.tarasov.bank.common.client;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

public abstract class AbstractServiceClient {

    private RestClient restClient;
    private final String serviceRegistrationId;
    private final String serviceUrl;

    @Autowired
    private OAuth2AuthorizedClientManager authorizedClientManager;

    @Autowired
    private RestClient.Builder lbRestClientBuilder;

    public AbstractServiceClient(String serviceUrl,
                                 String serviceRegistrationId) {
        this.serviceUrl = serviceUrl;
        this.serviceRegistrationId = serviceRegistrationId;

    }

    @PostConstruct
    public void init() {
        this.restClient = lbRestClientBuilder
                .baseUrl(serviceUrl)
                .build();
    }

    private RestClient.ResponseSpec abstractPost(String uri, Object body) {
        String authToken = getAuthToken();
        return restClient.post()
                .uri(uri)
                .headers(h -> h.setBearerAuth(authToken))
                .body(body)
                .retrieve();
    }

    public void post(String uri, Object body) {
        abstractPost(uri, body)
                .toBodilessEntity();
    }

    public <T> T post(String uri, Object body, Class<T> responseType) {
        return abstractPost(uri, body)
                .body(responseType);
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

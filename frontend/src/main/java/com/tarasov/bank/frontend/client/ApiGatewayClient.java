package com.tarasov.bank.frontend.client;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ApiGatewayClient {

    private final RestClient apiGWRestClient;
    private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    public RestClient.RequestHeadersSpec<?> get(String uri) {
        var spec = apiGWRestClient.get().uri(uri);
        String authToken = getAuthToken();
        if (authToken != null) {
            spec.header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken);
        }
        return spec;
    }

    public RestClient.RequestBodySpec post(String uri) {
        var spec = apiGWRestClient.post().uri(uri);
        String authToken = getAuthToken();
        if (authToken != null) {
            spec.header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken);
        }
        return spec;
    }

    private String getAuthToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
            OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                    .withClientRegistrationId(oauth2Token.getAuthorizedClientRegistrationId())
                    .principal(oauth2Token)
                    .build();
            OAuth2AuthorizedClient client = oAuth2AuthorizedClientManager.authorize(request);

            if (client != null && client.getAccessToken() != null) {
                return client.getAccessToken().getTokenValue();
            }
        }
        return null;
    }
}

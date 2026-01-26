package com.tarasov.bank.frontend.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    private final String apiGWUrl;

    public RestClientConfig(@Value("${gateway.url}")
                            String apiGatewayUrl) {
        this.apiGWUrl = apiGatewayUrl;
    }

    @Bean
    @LoadBalanced
    RestClient.Builder apiGwRestClientBuilder() {
        return RestClient.builder()
                .baseUrl(apiGWUrl);
    }

    @Bean
    public RestClient apiGWRestClient() {
        return apiGwRestClientBuilder()
                .build();
    }
}

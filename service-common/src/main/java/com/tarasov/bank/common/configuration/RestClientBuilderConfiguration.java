package com.tarasov.bank.common.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
public class RestClientBuilderConfiguration {

    @Bean
    @LoadBalanced
    public RestClient.Builder lbRestClientBuilder() {
        return RestClient.builder();
    }
}

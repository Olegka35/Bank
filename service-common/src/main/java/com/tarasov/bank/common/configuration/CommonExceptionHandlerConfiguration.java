package com.tarasov.bank.common.configuration;

import com.tarasov.bank.common.controller.CommonExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
public class CommonExceptionHandlerConfiguration {

    @Bean
    public CommonExceptionHandler commonExceptionHandler() {
        return new CommonExceptionHandler();
    }
}

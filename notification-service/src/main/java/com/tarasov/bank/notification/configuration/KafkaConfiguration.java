package com.tarasov.bank.notification.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;

@Configuration
public class KafkaConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfiguration.class);

    @Bean
    KafkaListenerErrorHandler notificationListenerErrorHandler() {
        return (message, e) -> {
            LOGGER.error("Error while processing notification ({}): ", message, e);
            return null;
        };
    }
}

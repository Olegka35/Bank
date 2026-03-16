package com.tarasov.bank.cash.producer;

import com.tarasov.bank.cash.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class KafkaNotificationProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaNotificationProducer.class);

    private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    public void sendNotification(NotificationRequest notificationRequest) {
        LOGGER.info("Notification sent to Kafka: {}",  notificationRequest);
        kafkaTemplate.send("cash-notifications", notificationRequest.login(), notificationRequest);
    }
}

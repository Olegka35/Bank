package com.tarasov.bank.transfer.producer;

import com.tarasov.bank.transfer.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Component
@RequiredArgsConstructor
public class KafkaNotificationProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaNotificationProducer.class);

    private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    public void sendNotification(NotificationRequest notificationRequest) {
        LOGGER.info("Notification sent to Kafka: {}",  notificationRequest);
        ProducerRecord<String, NotificationRequest> record =
                new ProducerRecord<>("transfer-notifications", notificationRequest.login(), notificationRequest);
        record.headers()
                .add("traceId", MDC.get("traceId").getBytes(StandardCharsets.UTF_8))
                .add("spanId", MDC.get("spanId").getBytes(StandardCharsets.UTF_8))
                .add("username", MDC.get("username").getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(record);
    }
}

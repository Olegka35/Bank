package com.tarasov.bank.cash.producer;

import com.tarasov.bank.cash.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class KafkaNotificationProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaNotificationProducer.class);

    private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    public void sendNotification(NotificationRequest notificationRequest) {
        LOGGER.info("Notification sent to Kafka: {}",  notificationRequest);
        ProducerRecord<String, NotificationRequest> record =
                new ProducerRecord<>("cash-notifications", notificationRequest.login(), notificationRequest);
        fillHeaderFromMDC(record, "traceId");
        fillHeaderFromMDC(record, "spanId");
        fillHeaderFromMDC(record, "username");
        kafkaTemplate.send(record);
    }

    private void fillHeaderFromMDC(ProducerRecord<String, NotificationRequest> record, String attr) {
        Optional.ofNullable(MDC.get(attr))
                .ifPresent(v -> record.headers().add(attr, v.getBytes(StandardCharsets.UTF_8)));
    }
}

package com.tarasov.bank.notification.consumer;

import com.tarasov.bank.notification.dto.NotificationRequest;
import com.tarasov.bank.notification.service.NotificationProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationProcessService notificationProcessService;

    @KafkaListener(topics = { "transer-notifications", "cash-notifications", "account-notifications" },
            errorHandler = "notificationListenerErrorHandler")
    public void processNotification(@Payload NotificationRequest notificationRequest) {
        notificationProcessService.processNotification(notificationRequest);
    }
}

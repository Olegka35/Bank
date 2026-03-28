package com.tarasov.bank.notification.consumer;

import com.tarasov.bank.notification.dto.NotificationRequest;
import com.tarasov.bank.notification.service.NotificationProcessService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationProcessService notificationProcessService;

    @KafkaListener(topics = { "transfer-notifications", "cash-notifications", "account-notifications" },
            errorHandler = "notificationListenerErrorHandler")
    public void processNotification(@Payload NotificationRequest notificationRequest,
                                    @Header("traceId") String traceId,
                                    @Header("spanId") String spanId,
                                    @Header("username") String username) {
        MDC.put("traceId", traceId);
        MDC.put("spanId", spanId);
        MDC.put("username", username);
        try {
            notificationProcessService.processNotification(notificationRequest);
        } finally {
            MDC.remove("traceId");
            MDC.remove("spanId");
            MDC.remove("username");
        }
    }
}

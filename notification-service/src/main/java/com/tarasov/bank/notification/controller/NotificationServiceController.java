package com.tarasov.bank.notification.controller;


import com.tarasov.bank.notification.dto.NotificationRequest;
import com.tarasov.bank.notification.service.NotificationProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
public class NotificationServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceController.class);

    private final NotificationProcessService notificationProcessService;

    @PostMapping("/notify")
    public void processNotification(@RequestBody @Valid NotificationRequest notificationRequest) {
        notificationProcessService.processNotification(notificationRequest);
    }
}

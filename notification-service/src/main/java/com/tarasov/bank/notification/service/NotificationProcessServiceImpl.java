package com.tarasov.bank.notification.service;

import com.tarasov.bank.notification.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class NotificationProcessServiceImpl implements NotificationProcessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationProcessServiceImpl.class);

    @Override
    public void processNotification(NotificationRequest notificationRequest) {
        LOGGER.info(notificationRequest.message());
    }
}

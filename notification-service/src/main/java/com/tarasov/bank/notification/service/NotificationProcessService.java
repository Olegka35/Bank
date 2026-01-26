package com.tarasov.bank.notification.service;


import com.tarasov.bank.notification.dto.NotificationRequest;

public interface NotificationProcessService {

    void processNotification(NotificationRequest notificationRequest);
}

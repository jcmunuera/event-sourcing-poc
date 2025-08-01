package com.jcm.financialapi.service;

import com.jcm.financialcontract.dto.NotificationRequest;

public interface NotificationService {
    void sendNotification(NotificationRequest request);
}

package com.jcm.financialapi.service;

import com.jcm.financialcontract.dto.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNotification(NotificationRequest request) {
         System.out.println( "NotificationService: sendNotification() - contact: " + request.getRecipientContact() + 
                                ", method: " + request.getMethod() + 
                                ", message: " + request.getMessage());
        // Integration with API for email/SMS/push
    }
}

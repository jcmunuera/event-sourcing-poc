package com.jcm.financialapi.controller;

import com.jcm.financialcontract.dto.NotificationRequest;
import com.jcm.financialapi.service.NotificationService;
import com.jcm.prospective.aspect.GenerateAuditEvent;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @GenerateAuditEvent
    public void sendNotification(@RequestBody NotificationRequest request) {
        notificationService.sendNotification(request);
    }
}

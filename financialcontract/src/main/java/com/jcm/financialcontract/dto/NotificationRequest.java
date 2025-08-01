package com.jcm.financialcontract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private String recipientContact; // Email, teléfono, token push
    private String method;           // "EMAIL", "SMS", "PUSH"
    private String message;          // Contenido de la notificación
}

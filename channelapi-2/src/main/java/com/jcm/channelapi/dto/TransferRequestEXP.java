package com.jcm.channelapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class TransferRequestEXP {
    private String fromAccountId;
    private String toAccountId;
    private double amount;
    private String notificationMethod; // "EMAIL", "SMS", "PUSH"
    private String recipientContact;   // email o número teléfono
}

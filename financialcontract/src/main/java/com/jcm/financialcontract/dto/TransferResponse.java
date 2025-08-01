package com.jcm.financialcontract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class TransferResponse {
    private String transactionId;
    private String status; // e.g., "success", "failed"
}

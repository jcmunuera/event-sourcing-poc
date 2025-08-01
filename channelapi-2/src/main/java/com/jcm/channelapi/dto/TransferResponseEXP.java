package com.jcm.channelapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class TransferResponseEXP {
    private String transactionId;
    private String status;
}

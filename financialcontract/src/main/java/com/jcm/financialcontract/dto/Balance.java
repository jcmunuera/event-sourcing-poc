package com.jcm.financialcontract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor 
public class Balance {
    private String accountId;
    private double balance;
}

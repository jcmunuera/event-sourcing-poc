package com.jcm.financialcontract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class Movement {
    private String movementId;     // Unique ID for the movement (optional)
    private String description;    // Concept or description
    private double amount;         // Amount (+ for deposits, - for withdrawals)
    private LocalDate date;        // Date of the movement
}


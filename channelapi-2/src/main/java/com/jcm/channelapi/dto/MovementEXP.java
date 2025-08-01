package com.jcm.channelapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class MovementEXP {
    private String movementId;   // Unique ID for the movement
    private String description;  // Concept or description
    private double amount;       // Amount (+ deposits, - withdrawals
    private LocalDate date;      // Date of the movement
}

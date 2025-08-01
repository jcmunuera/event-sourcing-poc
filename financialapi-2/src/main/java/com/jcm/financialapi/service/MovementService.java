package com.jcm.financialapi.service;

import com.jcm.financialcontract.dto.Movement;

import java.util.List;

public interface MovementService {
    List<Movement> getMovements(String accountId, String from, String to);
}


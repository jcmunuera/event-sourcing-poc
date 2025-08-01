package com.jcm.financialapi.controller;

import com.jcm.financialcontract.dto.Movement;
import com.jcm.financialapi.service.MovementService;
import com.jcm.prospective.aspect.GenerateAuditEvent;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts/{accountId}/movements")
public class MovementsController {

    private final MovementService movementService;

    public MovementsController(MovementService movementService) {
        this.movementService = movementService;
    }

    @GetMapping
    @GenerateAuditEvent
    public List<Movement> getMovements(@PathVariable String accountId,
                                       @RequestParam(required = false) String from,
                                       @RequestParam(required = false) String to) {
        return movementService.getMovements(accountId, from, to);
    }
}

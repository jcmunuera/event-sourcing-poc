package com.jcm.financialapi.controller;

import com.jcm.financialcontract.dto.TransferRequest;
import com.jcm.financialcontract.dto.TransferResponse;
import com.jcm.financialapi.service.TransferService;
import com.jcm.prospective.aspect.GenerateAuditEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
public class TransfersController {

    private final TransferService transferService;

    public TransfersController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    @GenerateAuditEvent
    public TransferResponse makeTransfer(@RequestBody TransferRequest request) {
        return transferService.makeTransfer(request);
    }
}

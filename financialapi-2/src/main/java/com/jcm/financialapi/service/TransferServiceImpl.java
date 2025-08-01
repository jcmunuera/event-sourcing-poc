package com.jcm.financialapi.service;

import com.jcm.financialcontract.dto.TransferRequest;
import com.jcm.financialcontract.dto.TransferResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransferServiceImpl implements TransferService {

    @Override
    public TransferResponse makeTransfer(TransferRequest request) {
        String transactionId = UUID.randomUUID().toString();
        System.out.println("TransferService: makeTransfer() - Processing transfer: " + request.getAmount()
                + " from " + request.getFromAccountId()
                + " to " + request.getToAccountId());
        // Aquí puedes agregar lógica real o publicar evento Kafka
        return new TransferResponse(transactionId, "success");
    }
}

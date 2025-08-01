package com.jcm.financialapi.service;

import com.jcm.financialcontract.dto.TransferRequest;
import com.jcm.financialcontract.dto.TransferResponse;

public interface TransferService {
    TransferResponse makeTransfer(TransferRequest request);
}

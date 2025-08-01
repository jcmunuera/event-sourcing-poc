package com.jcm.financialapi.service;

import com.jcm.financialcontract.dto.Account;
import com.jcm.financialcontract.dto.Balance;

import java.util.List;

public interface AccountService {
    List<Account> getAccountsByUser(String userId);
    Balance getAccountBalance(String accountId);
}

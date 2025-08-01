package com.jcm.financialapi.service;

import com.jcm.financialcontract.dto.Account;
import com.jcm.financialcontract.dto.Balance;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public List<Account> getAccountsByUser(String userId) {
        System.out.println( "AccountService: getAccountsByUser() - userId: " + userId);
        // Mock: devolver cuentas ficticias
        return Arrays.asList(
            new Account("acc-001", "savings", 10250.75),
            new Account("acc-002", "checking", 2450.00)
        );
    }

    @Override 
    public Balance getAccountBalance(String accountId) {
         System.out.println( "AccountService: getAccountBalance() - accountId: " + accountId);
        // Mock: saldo aleatorio
        return new Balance(accountId, Math.random() * 20000);
    }
}

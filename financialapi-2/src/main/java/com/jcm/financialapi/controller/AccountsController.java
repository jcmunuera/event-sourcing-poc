package com.jcm.financialapi.controller;

import com.jcm.financialcontract.dto.Account;
import com.jcm.financialcontract.dto.Balance;
import com.jcm.financialapi.service.AccountService;
import com.jcm.prospective.aspect.GenerateAuditEvent;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final AccountService accountService;

    public AccountsController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @GenerateAuditEvent
    public List<Account> getAccounts(@RequestParam String userId) {
        return accountService.getAccountsByUser(userId);
    }

    @GetMapping("/{accountId}/balance")
    @GenerateAuditEvent
    public Balance getBalance(@PathVariable String accountId) {
        return accountService.getAccountBalance(accountId);
    }
}

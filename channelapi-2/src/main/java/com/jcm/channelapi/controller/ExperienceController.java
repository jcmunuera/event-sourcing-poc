package com.jcm.channelapi.controller;

import com.jcm.financialcontract.dto.*;
import com.jcm.channelapi.dto.*;
import com.jcm.channelapi.service.ExperienceService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcm.prospective.aspect.GenerateAuditEvent;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExperienceController {

    private static final Logger logger = LoggerFactory.getLogger(ExperienceController.class);

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @PostMapping("/login")
    @GenerateAuditEvent
    public UserSessionEXP login(@RequestBody UserLoginRequest request) {
        logger.info("ExperienceController - Login request for user: {}", request.getUsername());
        UserSessionEXP response = experienceService.login(request.getUsername(), request.getPassword());

        com.jcm.common.context.UserContext.setUserId(request.getUsername());
        com.jcm.common.context.UserContext.setSessionId(response.getSessionId());
        logger.info("ExperienceController - Setting UserContext userId: {} | sessionId: {}", request.getUsername(), response.getSessionId());

        return response;
    }

    @GetMapping("/landing")
    @GenerateAuditEvent
    public List<AccountSummaryEXP> getLanding(@RequestParam String userId) {
        logger.info("ExperienceController - Landing page request for user: {}", userId);
        return experienceService.loadLandingPage(userId);
    }

    @PostMapping("/transfer")
    @GenerateAuditEvent
    public TransferResponseEXP transfer(@RequestBody TransferRequestEXP request) {
        logger.info("ExperienceController - Transfer request from account: {}", request.getFromAccountId());
        return experienceService.executeTransfer(request);
    }

    @GetMapping("/accounts/{accountId}/movements")
    @GenerateAuditEvent
    public List<MovementEXP> getMovements(@PathVariable String accountId,
                                       @RequestParam String from,
                                       @RequestParam String to) {
        logger.info("ExperienceController - Movements request for account: {}", accountId);
        return experienceService.getAccountMovements(accountId, from, to);
    }
}


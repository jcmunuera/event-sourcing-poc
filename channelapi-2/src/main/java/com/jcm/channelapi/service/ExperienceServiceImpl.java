package com.jcm.channelapi.service;

import com.jcm.financialcontract.dto.*;
import com.jcm.channelapi.dto.*;
import com.jcm.channelapi.mapper.ExperienceMapper;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.client.RestTemplate;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ExperienceServiceImpl implements ExperienceService {
    private final RestTemplate restTemplate;
    private final ExperienceMapper experienceMapper;

    @Value("${financial.api.url:http://localhost:8080}")
    private String financialApiUrl;

    public ExperienceServiceImpl(RestTemplate restTemplate, ExperienceMapper experienceMapper) {
        this.restTemplate = restTemplate;
        this.experienceMapper = experienceMapper;
    }

    @Override
    public UserSessionEXP login(String username, String password) {
        System.out.println("ExperienceService: login() - user " + username);
        String url = financialApiUrl + "/users/login";
        UserLoginRequest request = new UserLoginRequest(username, password);
        UserLoginResponse response = restTemplate.postForObject(url, request, UserLoginResponse.class);
        return experienceMapper.toUserSessionEXP(response);
    }

    @Override
    public List<AccountSummaryEXP> loadLandingPage(String userId) {
        System.out.println("ExperienceService: loadLandingPage() - user: " + userId);
        String url = String.format(financialApiUrl + "/accounts?userId=%s", userId);
        Account[] accounts = restTemplate.getForObject(url, Account[].class);
        return experienceMapper.toAccountSummaryEXPList(Arrays.asList(accounts));
    }

    @Override
    public TransferResponseEXP executeTransfer(TransferRequestEXP request) {
        System.out.println( "ExperienceService: executeTransfer() - from: " + request.getFromAccountId());
        String url = financialApiUrl + "/transfers";
        TransferRequest domainRequest =  new TransferRequest(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
        TransferResponse response = restTemplate.postForObject(url, domainRequest, TransferResponse.class);

        // Notify if needed
        if (request.getNotificationMethod() != null && request.getRecipientContact() != null) {
            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.setMethod(request.getNotificationMethod());
            notificationRequest.setRecipientContact(request.getRecipientContact());
            notificationRequest.setMessage("Transfer for rental car survey");
            restTemplate.postForObject(financialApiUrl + "/notifications", notificationRequest, Void.class); 
        }

        return experienceMapper.toTransferResponseEXP(response);
    }

    @Override
    public List<MovementEXP> getAccountMovements(String accountId, String from, String to) {
        System.out.println( "ExperienceService: getAccountMovements() - account: " + accountId);
        String url = String.format(financialApiUrl + "/accounts/%s/movements?from=%s&to=%s", accountId, from, to);
        Movement[] movements = restTemplate.getForObject(url, Movement[].class);
        return experienceMapper.toMovementEXPList(Arrays.asList(movements));
    }
}


package com.jcm.channelapi.mapper;

import com.jcm.financialcontract.dto.*;
import com.jcm.channelapi.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class ExperienceMapper {

    public UserSessionEXP toUserSessionEXP(UserLoginResponse response) {
        return new UserSessionEXP(response.getSessionId(), LocalDateTime.now().toString());
    }

    public List<AccountSummaryEXP> toAccountSummaryEXPList(List<Account> accounts) {
        return accounts.stream()
                .map(acc -> new AccountSummaryEXP(acc.getAccountId(), acc.getAccountType(), acc.getBalance()))
                .collect(Collectors.toList());
    }

    public TransferResponseEXP toTransferResponseEXP(TransferResponse response) {
        return new TransferResponseEXP(response.getTransactionId(), response.getStatus());
    }

    public List<MovementEXP> toMovementEXPList(List<Movement> movements) {
        return movements.stream()
                .map(mov -> new MovementEXP(mov.getMovementId(), mov.getDescription(), mov.getAmount(), mov.getDate()))
                .collect(Collectors.toList());
    }
}


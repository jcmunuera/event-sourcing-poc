package com.jcm.channelapi.service;

import com.jcm.channelapi.dto.*;
import java.util.List;

public interface ExperienceService {
    UserSessionEXP login(String username, String password);
    List<AccountSummaryEXP> loadLandingPage(String userId);
    TransferResponseEXP executeTransfer(TransferRequestEXP request);
    List<MovementEXP> getAccountMovements(String accountId, String from, String to);
}

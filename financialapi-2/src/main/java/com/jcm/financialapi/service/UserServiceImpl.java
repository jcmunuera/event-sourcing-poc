package com.jcm.financialapi.service;

import com.jcm.financialcontract.dto.UserLoginResponse;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Override
    public UserLoginResponse login(String username, String password) {
        logger.info("UserService: login() - user: {}", username);
        String sessionId = "session-" + UUID.randomUUID();

        com.jcm.common.context.UserContext.setUserId(username);
        com.jcm.common.context.UserContext.setSessionId(sessionId);
        logger.info("UserService: login() - Setting UserContext userId: {} | sessionId: {}", username, sessionId);

        return new UserLoginResponse(sessionId);
    }
}

package com.jcm.financialapi.service;

import com.jcm.financialcontract.dto.UserLoginResponse;

public interface UserService {
    UserLoginResponse login(String username, String password);
}

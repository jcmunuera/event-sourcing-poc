package com.jcm.financialapi.controller;

import com.jcm.financialcontract.dto.UserLoginRequest;
import com.jcm.financialcontract.dto.UserLoginResponse;
import com.jcm.financialapi.service.UserService;
import com.jcm.prospective.aspect.GenerateAuditEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @GenerateAuditEvent
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        UserLoginResponse response = userService.login(request.getUsername(), request.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Session-Id", response.getSessionId());

        return ResponseEntity.ok().headers(headers).body(response);  
    }
}





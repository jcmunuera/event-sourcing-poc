package com.jcm.auditevent.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawAuditEvent {
    private String eventType;
    private String timestamp;
    private String method;
    private String sessionId;
    private String userId;
    private Map<String, Object> arguments;
    private Map<String, Object> result;

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
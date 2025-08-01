package com.jcm.auditevent;

import java.time.Instant;
import java.util.Map;

public class UserLoggedInEvent {
    private String sessionId;
    private String username;
    private Instant timestamp;
    private Map<String, Object> payload;

    public UserLoggedInEvent(String sessionId, String username, Instant timestamp, Map<String, Object> payload) {
        this.sessionId = sessionId;
        this.username = username;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public String getSessionId() { return sessionId; }
    public String getUsername() { return username; }
    public Instant getTimestamp() { return timestamp; }
    public Map<String, Object> getPayload() { return payload; }
}

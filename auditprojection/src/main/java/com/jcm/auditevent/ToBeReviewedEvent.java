package com.jcm.auditevent;

import java.time.Instant;
import java.util.Map;

public class ToBeReviewedEvent {
    private String sessionId;
    private String userId;
    private Instant timestamp;
    private Map<String, Object> payload;

    public ToBeReviewedEvent(String sessionId, String userId, Instant timestamp, Map<String, Object> payload) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public String getSessionId() { return sessionId; }
    public String getUserId() { return userId; }
    public Instant getTimestamp() { return timestamp; }
    public Map<String, Object> getPayload() { return payload; }
}

package com.jcm.auditeventLC.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@Getter
public class ToBeReviewedEvent implements DomainEvent {
    private final String eventType = "ToBeReviewed";
    private final String sessionId;
    private final String userId;
    private final LocalDateTime timestamp;
    private final Map<String, Object> details;

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public Map<String, Object> getDetails() {
        return details;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
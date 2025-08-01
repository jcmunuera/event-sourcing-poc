package com.jcm.auditevent.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserLoggedInEvent implements DomainEvent {
    private String eventType = "UserLoggedIn";
    private String sessionId;
    private String userId;
    private LocalDateTime timestamp;
    private Map<String, Object> details;

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
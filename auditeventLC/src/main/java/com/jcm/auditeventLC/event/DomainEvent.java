package com.jcm.auditeventLC.event;

import java.time.LocalDateTime;
import java.util.Map;

public interface DomainEvent {
    String getEventType();
    String getSessionId();
    String getUserId();
    LocalDateTime getTimestamp();
    Map<String, Object> getDetails();
}
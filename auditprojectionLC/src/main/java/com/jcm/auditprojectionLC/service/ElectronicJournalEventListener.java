package com.jcm.auditprojectionLC.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcm.auditprojectionLC.dto.DebeziumChangeEvent;
import com.jcm.auditprojectionLC.event.DomainEvent;
import com.jcm.auditprojectionLC.event.ToBeReviewedEvent;
import com.jcm.auditprojectionLC.event.UserLoggedInEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Service
@Slf4j
public class ElectronicJournalEventListener {

    private final ObjectMapper objectMapper;
    private final ElectronicJournalProjectionService projectionService;

    public ElectronicJournalEventListener(ObjectMapper objectMapper, ElectronicJournalProjectionService projectionService) {
        this.objectMapper = objectMapper;
        this.projectionService = projectionService;
    }

    @KafkaListener(topics = "electronic-journal-projection", groupId = "auditprojection-lc-group")
    public void listen(String message) {
        try {
            log.info("(@JCM) Received message from electronic-journal-projection: {}", message);
            
            DebeziumChangeEvent changeEvent = objectMapper.readValue(message, DebeziumChangeEvent.class);
            
            if ("c".equals(changeEvent.getOperation()) && changeEvent.getAfter() != null) {
                DomainEvent domainEvent = mapToDomainEvent(changeEvent.getAfter());
                if (domainEvent != null) {
                    projectionService.processDomainEvent(domainEvent);
                    log.info("Successfully processed domain event: {}", domainEvent.getEventType());
                } else {
                    log.warn("Could not map change event to domain event: {}", message);
                }
            } else {
                log.debug("Ignoring non-insert operation: {}", changeEvent.getOperation());
            }
            
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }

    private DomainEvent mapToDomainEvent(Map<String, Object> eventData) {
        try {
            String eventType = (String) eventData.get("event_type");
            if (eventType == null) {
                log.warn("Event type is null in event data: {}", eventData);
                return null;
            }

            String sessionId = (String) eventData.get("session_id");
            String userId = (String) eventData.get("user_id");
            String timestampStr = (String) eventData.get("timestamp");
            LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.parse(timestampStr), ZoneId.systemDefault());
            
            String detailsJson = (String) eventData.get("details");
            Map<String, Object> details = objectMapper.readValue(detailsJson, Map.class);

            switch (eventType) {
                case "UserLoggedIn":
                    return UserLoggedInEvent.builder()
                            .eventType(eventType)
                            .sessionId(sessionId)
                            .userId(userId)
                            .timestamp(timestamp)
                            .details(details)
                            .build();
                case "ToBeReviewed":
                    return ToBeReviewedEvent.builder()
                            .eventType(eventType)
                            .sessionId(sessionId)
                            .userId(userId)
                            .timestamp(timestamp)
                            .details(details)
                            .build();
                default:
                    log.warn("Unknown event type: {}", eventType);
                    return null;
            }
        } catch (Exception e) {
            log.error("Error mapping event data to domain event", e);
            return null;
        }
    }
}
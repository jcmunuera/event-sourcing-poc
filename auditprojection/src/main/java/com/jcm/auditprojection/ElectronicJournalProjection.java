package com.jcm.auditprojection;

import org.axonframework.eventhandling.EventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

import com.jcm.auditevent.event.UserLoggedInEvent;
import com.jcm.auditevent.event.ToBeReviewedEvent;

@Component
public class ElectronicJournalProjection {

    private final AuditLogEntryRepository auditLogEntryRepository;
    private final ObjectMapper objectMapper;

    public ElectronicJournalProjection(AuditLogEntryRepository auditLogEntryRepository, ObjectMapper objectMapper) {
        this.auditLogEntryRepository = auditLogEntryRepository;
        this.objectMapper = objectMapper;
    }

    @EventHandler
    public void on(UserLoggedInEvent event) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(event.getDetails());
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        AuditLogEntry entry = new AuditLogEntry(
                UUID.randomUUID(),
                event.getEventType(),
                event.getSessionId(),
                event.getUserId(),
                event.getTimestamp().toInstant(java.time.ZoneOffset.UTC),
                payload,
                Instant.now()
        );
        auditLogEntryRepository.save(entry);
    }

    @EventHandler
    public void on(ToBeReviewedEvent event) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(event.getDetails());
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        AuditLogEntry entry = new AuditLogEntry(
                UUID.randomUUID(),
                event.getEventType(),
                event.getSessionId(),
                event.getUserId(),
                event.getTimestamp().toInstant(java.time.ZoneOffset.UTC),
                payload,
                Instant.now()
        );
        auditLogEntryRepository.save(entry);
    }
}

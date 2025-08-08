package com.jcm.auditprojectionLC.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcm.auditprojectionLC.entity.AuditLogEntry;
import com.jcm.auditprojectionLC.event.DomainEvent;
import com.jcm.auditprojectionLC.repository.AuditLogEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class ElectronicJournalProjectionService {

    private final AuditLogEntryRepository auditLogEntryRepository;
    private final ObjectMapper objectMapper;

    public ElectronicJournalProjectionService(AuditLogEntryRepository auditLogEntryRepository, ObjectMapper objectMapper) {
        this.auditLogEntryRepository = auditLogEntryRepository;
        this.objectMapper = objectMapper;
    }

    public void processDomainEvent(DomainEvent event) {
        try {
            log.debug("Processing domain event: {}", event.getEventType());
            
            String payload = objectMapper.writeValueAsString(event.getDetails());
            
            AuditLogEntry entry = new AuditLogEntry(
                    UUID.randomUUID(),
                    event.getEventType(),
                    event.getSessionId(),
                    event.getUserId(),
                    event.getTimestamp().atZone(java.time.ZoneId.systemDefault()).toInstant(),
                    payload,
                    Instant.now()
            );
            
            auditLogEntryRepository.save(entry);
            log.info("Successfully saved audit log entry for event type: {}, user: {}, session: {}", 
                    event.getEventType(), event.getUserId(), event.getSessionId());
            
        } catch (Exception e) {
            log.error("Error processing domain event: {}", event.getEventType(), e);
            throw new RuntimeException("Failed to process domain event", e);
        }
    }
}
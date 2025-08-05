package com.jcm.auditeventLC.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jcm.auditeventLC.config.EventMappingProperties;
import com.jcm.auditeventLC.dbo.DomainEventDbo;
import com.jcm.auditeventLC.dto.RawAuditEvent;
import com.jcm.auditeventLC.event.DomainEvent;
import com.jcm.auditeventLC.event.FundsTransferredEvent;
import com.jcm.auditeventLC.event.ToBeReviewedEvent;
import com.jcm.auditeventLC.event.UserLoggedInEvent;
import com.jcm.auditeventLC.repository.DomainEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AuditEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AuditEventProcessor.class);
    private final ObjectMapper objectMapper;
    private final EventMappingProperties eventMappingProperties;
    private final DomainEventRepository domainEventRepository;

    public AuditEventProcessor(EventMappingProperties eventMappingProperties, DomainEventRepository domainEventRepository) {
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.eventMappingProperties = eventMappingProperties;
        this.domainEventRepository = domainEventRepository;
    }

    @KafkaListener(topics = "audit-events-raw", groupId = "audit-group-lc")
    public void listen(String message) {
        logger.info("(@JCM) Received raw audit event: {}", message);
        try {
            RawAuditEvent rawEvent = objectMapper.readValue(message, RawAuditEvent.class);
            logger.info("(@JCM) Deserialized raw audit event: {}", rawEvent.toString());

            DomainEvent domainEvent = mapToDomainEvent(rawEvent);
            logger.info("Mapped to domain event: {}", domainEvent);

            DomainEventDbo dbo = new DomainEventDbo();
            dbo.setEventType(domainEvent.getEventType());
            dbo.setSessionId(domainEvent.getSessionId());
            dbo.setUserId(domainEvent.getUserId());
            dbo.setTimestamp(domainEvent.getTimestamp());
            dbo.setDetails(domainEvent.getDetails());

            domainEventRepository.save(dbo);
            logger.info("Saved domain event to database: {}", dbo);

        } catch (Exception e) {
            logger.error("Error processing raw audit event: {}", message, e);
        }
    }

    private DomainEvent mapToDomainEvent(RawAuditEvent rawEvent) {
        String methodName = rawEvent.getMethod();
        String eventType = eventMappingProperties.getMethodToEvent().getOrDefault(methodName, "ToBeReviewedEvent");

        String userId = extractUserId(rawEvent);
        String sessionId = extractSessionId(rawEvent);

        Map<String, Object> details = new HashMap<>();
        Optional.ofNullable(rawEvent.getArguments()).ifPresent(args -> details.put("arguments", args));
        Optional.ofNullable(rawEvent.getResult()).ifPresent(res -> details.put("result", res));

        LocalDateTime timestamp = LocalDateTime.parse(rawEvent.getTimestamp());

        switch (eventType) {
            case "UserLoggedInEvent":
                return UserLoggedInEvent.builder()
                        .sessionId(sessionId)
                        .userId(userId)
                        .timestamp(timestamp)
                        .details(details)
                        .build();
            case "FundsTransferredEvent":
                return FundsTransferredEvent.builder()
                        .sessionId(sessionId)
                        .userId(userId)
                        .timestamp(timestamp)
                        .details(details)
                        .build();
            case "ToBeReviewedEvent":
            default:
                return ToBeReviewedEvent.builder()
                        .sessionId(sessionId)
                        .userId(userId)
                        .timestamp(timestamp)
                        .details(details)
                        .build();
        }
    }

    private String extractUserId(RawAuditEvent rawEvent) {
        if (rawEvent.getUserId() != null) {
            return rawEvent.getUserId();
        }

        return null;
    }

    private String extractSessionId(RawAuditEvent rawEvent) {
        if (rawEvent.getSessionId() != null) {
            return rawEvent.getSessionId();
        }
        return null;
    }
}
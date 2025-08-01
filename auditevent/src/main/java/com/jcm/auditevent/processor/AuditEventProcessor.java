package com.jcm.auditevent.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jcm.auditevent.config.EventMappingProperties;
import com.jcm.auditevent.dto.RawAuditEvent;
import com.jcm.auditevent.event.DomainEvent;
import com.jcm.auditevent.event.FundsTransferredEvent;
import com.jcm.auditevent.event.ToBeReviewedEvent;
import com.jcm.auditevent.event.UserLoggedInEvent;
import org.axonframework.eventhandling.gateway.EventGateway;
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
    private final EventGateway eventGateway;

    public AuditEventProcessor(EventMappingProperties eventMappingProperties, EventGateway eventGateway) {
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.eventMappingProperties = eventMappingProperties;
        this.eventGateway = eventGateway;
    }

    @KafkaListener(topics = "audit-events-raw", groupId = "audit-group")
    public void listen(String message) {
        logger.info("(@JCM) Received raw audit event: {}", message);
        try {
            RawAuditEvent rawEvent = objectMapper.readValue(message, RawAuditEvent.class);
            logger.info("(@JCM) Deserialized raw audit event: {}", rawEvent.toString());
            logger.info("(@JCM) RawEvent.sessionId: " + rawEvent.getSessionId());
            logger.info("(@JCM) RawEvent.userId: " + rawEvent.getUserId());

            DomainEvent domainEvent = mapToDomainEvent(rawEvent);
            logger.info("Mapped to domain event: {}", domainEvent);

            eventGateway.publish(domainEvent);
            logger.info("Published domain event: {}", domainEvent.getClass().getSimpleName());
            logger.info("(@JCM) domain event: " + domainEvent.toString());
            logger.info("(@JCM) domain.sessionId: " + domainEvent.getSessionId());
            logger.info("(@JCM) domain.userId: " + domainEvent.getUserId());
        } catch (Exception e) {
            logger.error("Error processing raw audit event: {}", message, e);
            // Consider publishing a DeadLetterEvent or similar for failed processing
        }
    }

    private DomainEvent mapToDomainEvent(RawAuditEvent rawEvent) {
        String methodName = rawEvent.getMethod();
        String eventType = eventMappingProperties.getMethodToEvent().getOrDefault(methodName, "ToBeReviewedEvent");

        // Extract userId and sessionId from rawEvent arguments or result
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
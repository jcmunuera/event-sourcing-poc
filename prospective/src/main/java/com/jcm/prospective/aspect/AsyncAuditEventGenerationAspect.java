package com.jcm.prospective.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.Date;
import com.jcm.common.context.UserContext;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

@Aspect
@Component
public class AsyncAuditEventGenerationAspect {

    private static final Logger logger = LoggerFactory.getLogger(AsyncAuditEventGenerationAspect.class);

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final BlockingQueue<Map<String, Object>> eventQueue = new LinkedBlockingQueue<>();
    private ExecutorService executorService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final int THREAD_POOL_SIZE = 5; // Adjust based on your needs
    private static final String AUDIT_TOPIC = "audit-events-raw"; // Kafka topic name

    public AsyncAuditEventGenerationAspect(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void initialize() {
        executorService = new DelegatingSecurityContextExecutorService(
                new ThreadPoolExecutor(
                        THREAD_POOL_SIZE,
                        THREAD_POOL_SIZE,
                        0L,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(),
                        new ThreadPoolExecutor.CallerRunsPolicy() // Important: Handle queue overflow
                )
        );
        startEventConsumer();
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void startEventConsumer() {
        executorService.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Map<String, Object> event = eventQueue.take(); // Blocking
                    try {
                        String jsonEvent = mapper.writeValueAsString(event);
                        logger.info("Sending event to Kafka: {}", jsonEvent);
                        kafkaTemplate.send(AUDIT_TOPIC, jsonEvent); // Send to Kafka
                    } catch (Exception e) {
                        logger.error("Error processing event: {}", e.getMessage(), e);
                        // Consider retry logic, dead-letter queue, etc.
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    @AfterReturning(
            pointcut = "@annotation(com.jcm.prospective.aspect.GenerateAuditEvent)",
            returning = "result"
    )
    public void afterMethod(JoinPoint joinPoint, Object result) {
        logger.info("Aspect: afterMethod executed for {}", joinPoint.getSignature().getName());
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            GenerateAuditEvent config = method.getAnnotation(GenerateAuditEvent.class);
            boolean includeArgs = config.includeArguments();
            boolean includeRes = config.includeResponse();

            Object resultPayload = result;

            String methodName = method.getName();
            String timestamp = LocalDateTime.now().toString();

            Map<String, Object> event = new LinkedHashMap<>();
            event.put("eventType", "MethodExecuted");
            event.put("timestamp", timestamp);
            event.put("method", methodName);

            String sessionId = UserContext.getSessionId();
            String userId = UserContext.getUserId();

            logger.info("Aspect - UserContext values: userId={} | sessionId={}", userId, sessionId);

            if (sessionId != null) {
                event.put("sessionId", sessionId);
            }
            if (userId != null) {
                event.put("userId", userId);
            }


            if (includeArgs) {
                Map<String, Object> argsMap = new LinkedHashMap<>();
                Parameter[] params = method.getParameters();
                Object[] args = joinPoint.getArgs();

                for (int i = 0; i < params.length; i++) {
                    String paramName = params[i].getName();
                    Object arg = args[i];

                    if (params[i].isAnnotationPresent(SensitiveField.class)) {
                        SensitiveField sensitiveField = params[i].getAnnotation(SensitiveField.class);
                        if (sensitiveField.mask()) {
                            if (arg != null) { // Add null check
                                arg = maskValue(arg);
                            }
                        } else {
                            continue;
                        }
                    } else if (arg != null && !isSimpleType(arg.getClass())) {
                        // For DTO parameters, extract fields recursively
                        arg = extractFields(arg);
                    }
                    argsMap.put(paramName, arg);
                }
                event.put("arguments", argsMap);
            }

            if (includeRes) {
                Map<String, Object> resMap = null;
                if (resultPayload != null) {
                    if (isComplexType(resultPayload.getClass())) {
                        resMap = extractFields(resultPayload);
                    } else {
                        if (method.getAnnotation(GenerateAuditEvent.class) != null) {
                            GenerateAuditEvent auditEventAnnotation = method.getAnnotation(GenerateAuditEvent.class);
                            if (auditEventAnnotation.includeResponse()) {
                                if (resultPayload.getClass().isAnnotationPresent(SensitiveField.class)) {
                                    SensitiveField sensitiveField = resultPayload.getClass().getAnnotation(SensitiveField.class);
                                    if (sensitiveField.mask()) {
                                        resultPayload = maskValue(resultPayload);
                                    }
                                }
                                resMap = new LinkedHashMap<>();
                                resMap.put("result", resultPayload);
                            } else {
                                resMap = null;
                            }

                        } else {
                            resMap = null;
                        }
                    }
                } else {
                    resMap = null;
                }
                if (resMap != null) {
                    event.put("result", resMap);
                }
            }
            // Send event to queue for asynchronous processing
            eventQueue.put(event);

        } catch (Exception e) {
            logger.error("Error creating audit event: {}", e.getMessage(), e);
        }
    }

   private Map<String, Object> extractFields(Object dto) {
    Map<String, Object> resultMap = new LinkedHashMap<>();

    if (dto == null) {
        return resultMap;
    }

    Class<?> clazz = dto.getClass();

    if (dto instanceof List) { // Handle Lists
        List<?> list = (List<?>) dto;
        for (int i = 0; i < list.size(); i++) {
            Object element = list.get(i);
            if (element != null) {
                if (!isSimpleType(element.getClass())) {
                    resultMap.put("element[" + i + "]", extractFields(element)); // Recursive call for each element
                } else {
                    resultMap.put("element[" + i + "]", element);  //Put simple values directly
                }
            }
        }
    } else if (clazz.isArray()) { //Handle Arrays
        Object[] array = (Object[]) dto;
        for (int i = 0; i < array.length; i++) {
            Object element = array[i];
              if (element != null) {
                if (!isSimpleType(element.getClass())) {
                     resultMap.put("element[" + i + "]", extractFields(element)); // Recursive call for each element
                } else {
                    resultMap.put("element[" + i + "]", element); //Put simple values directly
                }
            }
        }

    }
    else { // Handle regular DTOs
        Field[] fields = fieldCache.computeIfAbsent(clazz, c -> c.getDeclaredFields());

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(SensitiveField.class)) {
                    SensitiveField sf = field.getAnnotation(SensitiveField.class);
                    if (sf.mask()) {
                        Object value = field.get(dto);
                        if (value != null) {
                            value = maskValue(value);
                        }
                        resultMap.put(field.getName(), value);
                    } else {
                        continue;
                    }
                } else {
                    Object value = field.get(dto);
                    resultMap.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                // Ignore, not all objects will have a getSessionId method
            }
        }
    }

    return resultMap;
}

    private Object maskValue(Object value) {
        if (value == null) {
            return null;
        }
        String valStr = value.toString();
        return valStr.length() > 4 ? "****" + valStr.substring(valStr.length() - 4) : "****";
    }

    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive()
                || String.class.isAssignableFrom(clazz)
                || Number.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz)
                || java.time.temporal.Temporal.class.isAssignableFrom(clazz);
    }

    private boolean isComplexType(Class<?> clazz) {
        return !isSimpleType(clazz);
    }

    private final Map<Class<?>, Field[]> fieldCache = new ConcurrentHashMap<>();
}
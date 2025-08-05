package com.jcm.auditeventLC.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Component
@ConfigurationProperties(prefix = "audit.event")
public class EventMappingProperties {
    private Map<String, String> mapping;

    public Map<String, String> getMethodToEvent() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }
}
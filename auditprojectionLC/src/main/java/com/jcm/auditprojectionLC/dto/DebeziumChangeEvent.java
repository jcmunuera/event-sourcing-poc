package com.jcm.auditprojectionLC.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class DebeziumChangeEvent {
    @JsonProperty("op")
    private String operation;
    
    @JsonProperty("after")
    private Map<String, Object> after;
    
    @JsonProperty("before")
    private Map<String, Object> before;
    
    @JsonProperty("source")
    private Map<String, Object> source;
    
    @JsonProperty("ts_ms")
    private Long timestamp;
}
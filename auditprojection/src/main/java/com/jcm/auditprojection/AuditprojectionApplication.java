package com.jcm.auditprojection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication
public class AuditprojectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditprojectionApplication.class, args);
	}

	@Bean
	public static ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

	@Bean
	@Primary
	public Serializer serializer(ObjectMapper objectMapper) {
		return JacksonSerializer.builder().objectMapper(objectMapper).build();
	}

}

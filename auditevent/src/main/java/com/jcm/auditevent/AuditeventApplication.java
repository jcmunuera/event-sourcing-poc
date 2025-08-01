package com.jcm.auditevent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication
public class AuditeventApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditeventApplication.class, args);
	}

	@Bean
	@Primary
	public Serializer serializer(ObjectMapper objectMapper) {
		return JacksonSerializer.builder().objectMapper(objectMapper).build();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}

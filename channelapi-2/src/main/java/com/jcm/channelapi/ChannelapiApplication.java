package com.jcm.channelapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jcm.channelapi", "com.jcm.common.context", "com.jcm.prospective", "com.jcm.channelapi.config"})
@EnableAspectJAutoProxy
public class ChannelapiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChannelapiApplication.class, args);
	}
}


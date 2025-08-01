package com.jcm.financialapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jcm.financialapi", "com.jcm.prospective", "com.jcm.common.context", "com.jcm.financialapi.config"})
@EnableAspectJAutoProxy
public class FinancialapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialapiApplication.class, args);
	}

}


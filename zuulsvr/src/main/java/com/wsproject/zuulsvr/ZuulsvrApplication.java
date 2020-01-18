package com.wsproject.zuulsvr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class ZuulsvrApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulsvrApplication.class, args);
	}
	
//	@Bean 
//	public HystrixCircuitBreakerFactory hystrixCircuitBreakerFactory() {
//		return new HystrixCircuitBreakerFactory();
//	}
//	
//	@Bean
//	public Customizer<HystrixCircuitBreakerFactory> customizer() {
//	    return factory -> factory.configure(builder -> builder.commandProperties(
//	                    HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)), "confsvr");
//	}
}

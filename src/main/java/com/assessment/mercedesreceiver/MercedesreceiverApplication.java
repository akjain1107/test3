package com.assessment.mercedesreceiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootApplication
public class MercedesreceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MercedesreceiverApplication.class, args);
	}
	@Configuration
	public static class RestClientConfiguration {
		@Bean
		RestTemplate restTemplate(ProtobufHttpMessageConverter hmc) {
			return new RestTemplate(Arrays.asList(hmc));
		}
		@Bean
		ProtobufHttpMessageConverter protobufHttpMessageConverter() {
			return new ProtobufHttpMessageConverter();
		}
	}
}

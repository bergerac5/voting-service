package com.online.voting.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages = "com.online.voting.voting.clients")
public class VotingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotingServiceApplication.class, args);
	}

}

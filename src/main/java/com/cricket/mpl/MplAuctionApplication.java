package com.cricket.mpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MplAuctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MplAuctionApplication.class, args);
	}

}

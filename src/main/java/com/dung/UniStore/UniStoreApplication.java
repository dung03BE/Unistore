package com.dung.UniStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync //chay dong bo
@EnableCaching
public class UniStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniStoreApplication.class, args);
	}

}

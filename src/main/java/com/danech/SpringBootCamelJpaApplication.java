package com.danech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.danech.model.User;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RestController
@Slf4j
public class SpringBootCamelJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCamelJpaApplication.class, args);
	}
	
	@PostMapping("users")
	public ResponseEntity<String> users(@RequestBody User user) throws InterruptedException{
		log.info("########## User in Rest API received #########");
		Thread.sleep(4000);
		log.info("User in Rest API received request body {}",user);
		return ResponseEntity.status(500).body("Error");
	}

}
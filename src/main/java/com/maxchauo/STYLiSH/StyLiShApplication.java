package com.maxchauo.STYLiSH;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
@RestController
public class StyLiShApplication {

	public static void main(String[] args) {
		SpringApplication.run(StyLiShApplication.class, args);
	}

	@GetMapping("/")
	public String sayHi(){
		return "hi i am max!";
	}
}

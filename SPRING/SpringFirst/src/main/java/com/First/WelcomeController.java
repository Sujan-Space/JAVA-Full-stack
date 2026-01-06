package com.First;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

	@GetMapping("/greeting")
	public String welcome() {
		return "Welcome to your first Spring Boot project in STS";
	}
}

package com.bfsi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bfsi.service.LoginAttemptService;

@RestController
public class BankingController {
    private final LoginAttemptService service;
    
    public BankingController(LoginAttemptService service) {
    	this.service=service;
    }
    
    @GetMapping("/account")
    public String account() {
    	return "BFSI Account Details - Authorized Access";
    }
    
}

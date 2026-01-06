package com.bfsi.service;

import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
	
	private static final Logger Logger = LogManager.getLogger(LoginAttemptService.class);
	private static final int MAX_ATTEMPT = 3;
	
	private final ConcurrentHashMap<String, Integer> attempts = new  ConcurrentHashMap<>();
	
     public void loginSucceeded(String user) {
		attempts.remove(user);
		Logger.info("Login success for user: {}",user);
	}
     
     public void loginFailed(String user) {
    	 int count = attempts.getOrDefault(user,0) + 1;
    	 attempts.put(user, count);
    	 Logger.warn("Login Failed {} times for user",count,user);
    	 
    	 
     }
     
     public boolean isBlocked(String user) {
    	 return attempts.getOrDefault(user, 0) >= MAX_ATTEMPT;
    	 
     }
     public void checkBlocked(String user) {
    	 if (isBlocked(user)) {
    		 Logger.error("user {} is BLOCKED after 3 failed attemps",user);
    		 throw new RuntimeException("User account Locked");
    	 }
     }
}

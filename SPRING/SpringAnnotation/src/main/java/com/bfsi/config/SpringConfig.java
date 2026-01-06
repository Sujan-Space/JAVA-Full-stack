package com.bfsi.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.bfsi")  //  scan all the packages for Spring beans
public class SpringConfig {
    
}

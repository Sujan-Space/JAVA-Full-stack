package com.bfsi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.bfsi.service.LoginAttemptService;


@Configuration
public class SecurityConfig {

    private final LoginAttemptService loginAttemptService;

    // Constructor injection for LoginAttemptService only
    public SecurityConfig(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/account")
                .authenticated()  // Requires authentication for '/account'
                .anyRequest().permitAll())  // Allows all other requests
            .formLogin(form -> form
                .permitAll()
                .defaultSuccessUrl("/account", true))  // Add form login with default success URL
            .httpBasic();  // Enable HTTP Basic Authentication
        return http.build();
    }

    // The PasswordEncoder bean is already defined below, so we don't need constructor injection for it
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("Admin@123"))  // Password encoded using injected encoder
            .roles("USER")  // Ensure roles are prefixed with "ROLE_"
            .build();

        return new InMemoryUserDetailsManager(user) {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                loginAttemptService.checkBlocked(username);
                return super.loadUserByUsername(username);
            }
        };
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        loginAttemptService.loginSucceeded(event.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        loginAttemptService.loginFailed(event.getAuthentication().getName());
    }
}

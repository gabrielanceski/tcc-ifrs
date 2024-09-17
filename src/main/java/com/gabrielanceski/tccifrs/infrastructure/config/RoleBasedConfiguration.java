package com.gabrielanceski.tccifrs.infrastructure.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

@Component
public class RoleBasedConfiguration {

    public void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/company/**").hasAnyRole("ADMIN", "MASTER")
            .requestMatchers("/user/**").hasAnyRole("ADMIN", "MASTER")
            .anyRequest().authenticated()
        );
    }

}

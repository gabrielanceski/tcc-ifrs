package com.gabrielanceski.tccifrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TccIfrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TccIfrsApplication.class, args);
    }

}

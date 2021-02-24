package com.wanderzhang.honeypot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author 78445
 */
@SpringBootApplication
@EnableAsync
public class HoneypotApplication {
    public static void main(String[] args) {
        SpringApplication.run(HoneypotApplication.class, args);
    }
}

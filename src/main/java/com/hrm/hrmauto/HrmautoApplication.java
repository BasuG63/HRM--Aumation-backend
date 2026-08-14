package com.hrm.hrmauto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HrmautoApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                HrmautoApplication.class,
                args
        );
    }
}
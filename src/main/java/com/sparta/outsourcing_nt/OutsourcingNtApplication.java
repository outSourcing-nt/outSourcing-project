package com.sparta.outsourcing_nt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
public class OutsourcingNtApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutsourcingNtApplication.class, args);
    }

}

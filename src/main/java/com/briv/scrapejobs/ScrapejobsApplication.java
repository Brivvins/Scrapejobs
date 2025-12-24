package com.briv.scrapejobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScrapejobsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrapejobsApplication.class, args);
    }
}

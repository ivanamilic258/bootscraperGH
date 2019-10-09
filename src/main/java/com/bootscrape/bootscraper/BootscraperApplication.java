package com.bootscrape.bootscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.bootscrape.bootscraper.repository")
public class BootscraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootscraperApplication.class, args);
    }

}

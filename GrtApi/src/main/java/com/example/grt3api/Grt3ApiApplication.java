package com.example.grt3api;

import com.example.grt3api.service.TwitchService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Grt3ApiApplication {

    private final TwitchService twitchService;

    public Grt3ApiApplication(TwitchService twitchService) {
        this.twitchService = twitchService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Grt3ApiApplication.class, args);
    }

}

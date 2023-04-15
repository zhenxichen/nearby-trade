package com.zxchen.nearby;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class NearbyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NearbyApplication.class, args);
    }

}

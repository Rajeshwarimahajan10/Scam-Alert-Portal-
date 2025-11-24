package com.scamalert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class ScamAlertPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScamAlertPortalApplication.class, args);
    }
}


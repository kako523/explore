package com.kako.onestep.explore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ExploreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExploreApplication.class, args);
    }
}

package com.samsungjeomja.dotflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class DotFlowApplication  {

    public static void main(String[] args) {

        SpringApplication.run(DotFlowApplication.class, args);

    }

}

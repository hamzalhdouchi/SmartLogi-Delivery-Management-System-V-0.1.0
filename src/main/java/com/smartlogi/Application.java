package com.smartlogi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);


        System.out.println("||========================================||");
        System.out.println("||      Server is working successfully    ||");
        System.out.println("||========================================||");
    }


}

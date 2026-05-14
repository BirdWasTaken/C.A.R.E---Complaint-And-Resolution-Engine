package com.care;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CareApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareApplication.class, args);
        System.out.println("====================================================");
        System.out.println("  C.A.R.E Backend is running!");
        System.out.println("  URL: http://localhost:8080");
        System.out.println("====================================================");
    }

}

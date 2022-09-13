package com.adamludzia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableWebMvc
@ComponentScan
public class InvoicingSystemApplication {

    public static void main(String[] args) {
        System.out.println("hello world!");
        SpringApplication.run(InvoicingSystemApplication.class, args);
    }

}



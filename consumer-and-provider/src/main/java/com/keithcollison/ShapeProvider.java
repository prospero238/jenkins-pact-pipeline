package com.keithcollison;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAutoConfiguration
public class ShapeProvider
{

    public static void main(String[] args) {

        SpringApplication.run(ShapeProvider.class, args);

    }
}

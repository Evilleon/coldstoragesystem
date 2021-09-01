package com.ryq.coldstoragesystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ryq.coldstoragesystem.mapper")
@SpringBootApplication
public class ColdstoragesystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColdstoragesystemApplication.class, args);
    }

}

package com.ryq.renting;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Ren YuQi
 * @Create 2021-12-22 15:15
 */
@MapperScan("com.ryq.renting.mapper")
@SpringBootApplication
public class rentingApplication {
    public static void main(String[] args) {
        SpringApplication.run(rentingApplication.class, args);
    }
}

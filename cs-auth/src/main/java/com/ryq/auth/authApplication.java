package com.ryq.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Ren YuQi
 * @Create 2021-12-22 15:14
 */
@MapperScan("com.ryq.auth.mapper")
@SpringBootApplication
public class authApplication {
    public static void main(String[] args) {
        SpringApplication.run(authApplication.class, args);
    }
}

package com.ryq.transport;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Ren YuQi
 * @Create 2021-12-22 15:18
 */
@MapperScan("com.ryq.transport.mapper")
@SpringBootApplication
public class transportApplication {
    public static void main(String[] args) {
        SpringApplication.run(transportApplication.class, args);
    }

}

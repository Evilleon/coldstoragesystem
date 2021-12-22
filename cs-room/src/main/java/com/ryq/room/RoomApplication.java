package com.ryq.room;

import com.ryq.common.CommonApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Ren YuQi
 * @Create 2021-12-22 11:17
 */
@MapperScan("com.ryq.room.mapper")
@SpringBootApplication
public class RoomApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomApplication.class, args);
    }
}

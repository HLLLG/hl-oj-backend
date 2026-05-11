package com.hl.hlojbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hl.hlojbackend.mapper")
public class HlOjBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HlOjBackendApplication.class, args);
    }

}

package com.walkingpet.sitter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.walkingpet.sitter", "com.walkingpet.common"})
@EnableDiscoveryClient
@MapperScan("com.walkingpet.sitter.mapper")
public class SitterServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SitterServiceApplication.class, args);
        System.out.println("\n==================== Sitter Service Started ====================\n");
    }
}

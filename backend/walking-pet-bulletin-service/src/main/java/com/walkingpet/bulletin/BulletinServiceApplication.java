package com.walkingpet.bulletin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.walkingpet.bulletin", "com.walkingpet.common"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.walkingpet.bulletin.mapper")
public class BulletinServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BulletinServiceApplication.class, args);
        System.out.println("\n==================== Bulletin Service Started ====================\n");
    }
}

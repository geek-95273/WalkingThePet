package com.walkingpet.pet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.walkingpet"})
@EnableDiscoveryClient
@MapperScan("com.walkingpet.pet.mapper")
public class PetServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetServiceApplication.class, args);
    }
}

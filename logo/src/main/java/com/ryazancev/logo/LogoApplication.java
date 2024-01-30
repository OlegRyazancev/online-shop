package com.ryazancev.logo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(
        basePackages = "com.ryazancev.clients"
)
@ComponentScan(
        basePackages = {
                "com.ryazancev.config",
                "com.ryazancev.logo"
        })
public class LogoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogoApplication.class, args);
    }
}
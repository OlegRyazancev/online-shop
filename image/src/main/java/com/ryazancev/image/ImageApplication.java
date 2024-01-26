package com.ryazancev.image;

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
                "com.ryazancev.image"
        })
public class ImageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageApplication.class, args);
    }
}

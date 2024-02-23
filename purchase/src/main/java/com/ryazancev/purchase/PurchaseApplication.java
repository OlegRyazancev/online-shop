package com.ryazancev.purchase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@EnableDiscoveryClient
@EnableFeignClients(
        basePackages = "com.ryazancev.clients"
)
@PropertySources({
        @PropertySource(
                "classpath:clients-${spring.profiles.active}.properties"
        )
})
@SpringBootApplication(
        scanBasePackages = {
                "com.ryazancev.config",
                "com.ryazancev.purchase"
        })
public class PurchaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PurchaseApplication.class, args);
    }
}

package com.ryazancev.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @author Oleg Ryazancev
 */

@EnableDiscoveryClient
@EnableFeignClients(
        basePackages = "com.ryazancev.common.clients"
)
@EnableCaching
@PropertySources({
        @PropertySource(
                "classpath:clients-${spring.profiles.active}.properties"
        )
})
@SpringBootApplication(
        scanBasePackages = {
                "com.ryazancev.common.config",
                "com.ryazancev.product"
        })
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}

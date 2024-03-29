package com.ryazancev.logo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@PropertySources({
        @PropertySource(
                "classpath:clients-${spring.profiles.active}.properties"
        )
})
@SpringBootApplication(
        scanBasePackages = {
                "com.ryazancev.common.config",
                "com.ryazancev.logo"
        })
public class LogoApplication {

    public static void main(final String[] args) {
        SpringApplication.run(LogoApplication.class, args);
    }
}

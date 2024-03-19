package com.ryazancev.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @author Oleg Ryazancev
 */

@SpringBootApplication
@EnableDiscoveryClient
@PropertySources({
        @PropertySource(
                "classpath:clients-${spring.profiles.active}.properties"
        )
})
public class MailApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MailApplication.class, args);
    }
}

package com.ryazancev.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Oleg Ryazancev
 */

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}

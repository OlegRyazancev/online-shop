package com.ryazancev.common.config;

import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.support.JsonFormWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oleg Ryazancev
 */

@Configuration
@Slf4j
public class FeignClientsConfiguration extends FeignClientProperties.FeignClientConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Bean
    JsonFormWriter jsonFormWriter() {
        return new JsonFormWriter();
    }
}

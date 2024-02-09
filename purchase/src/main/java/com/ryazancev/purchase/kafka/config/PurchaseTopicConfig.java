package com.ryazancev.purchase.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class PurchaseTopicConfig {

    @Value("${spring.kafka.topic.customer.update}")
    private String customerUpdateTopicName;

    @Value("${spring.kafka.topic.product.update}")
    private String productUpdateTopicName;


    @Bean
    public NewTopic customerUpdateEventsTopic() {

        return TopicBuilder
                .name(customerUpdateTopicName)
                .build();
    }

    @Bean
    public NewTopic productUpdateEventsTopic() {

        return TopicBuilder
                .name(productUpdateTopicName)
                .build();
    }
}

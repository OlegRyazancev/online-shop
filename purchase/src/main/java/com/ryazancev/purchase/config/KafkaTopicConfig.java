package com.ryazancev.purchase.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.customer}")
    private String customerTopicName;

    @Value("${spring.kafka.topic.product}")
    private String productTopicName;


    @Bean
    public NewTopic customerEventsTopic() {

        return TopicBuilder
                .name(customerTopicName)
                .build();
    }

    @Bean
    public NewTopic productEventsTopic() {

        return TopicBuilder
                .name(productTopicName)
                .build();
    }
}

package com.ryazancev.product.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.admin}")
    private String adminTopicName;

    @Value("${spring.kafka.topic.product.register}")
    private String registerProductTopicName;

    @Value("${spring.kafka.topic.product.update}")
    private String updateProductTopicName;


    @Bean
    public NewTopic adminEventsTopic() {
        return TopicBuilder
                .name(adminTopicName)
                .build();
    }

    @Bean
    public NewTopic updateProductTopic() {
        return TopicBuilder
                .name(updateProductTopicName)
                .build();
    }


    @Bean
    public NewTopic registerProductTopic() {
        return TopicBuilder
                .name(registerProductTopicName)
                .build();
    }
}

package com.ryazancev.admin.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AdminTopicConfig {

    @Value("${spring.kafka.topic.organization.register}")
    private String organizationTopicName;

    @Value("${spring.kafka.topic.product.register}")
    private String productTopicName;

    @Value("${spring.kafka.topic.admin}")
    private String adminTopicName;


    @Bean
    public NewTopic organizationRegisterEventsTopic() {
        return TopicBuilder
                .name(organizationTopicName)
                .build();
    }

    @Bean
    public NewTopic productRegisterEventsTopic() {
        return TopicBuilder
                .name(productTopicName)
                .build();
    }

    @Bean
    public NewTopic adminEventsTopic() {
        return TopicBuilder
                .name(adminTopicName)
                .build();
    }
}

package com.ryazancev.admin.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AdminTopicConfig {

    @Value("${spring.kafka.topic.organization.register}")
    private String organizationRegisterTopic;

    @Value("${spring.kafka.topic.product.register}")
    private String productRegisterTopic;

    @Value("${spring.kafka.topic.admin}")
    private String adminTopicName;

    @Value("${spring.kafka.topic.organization.change-status}")
    private String organizationChangeStatusTopic;

    @Value("${spring.kafka.topic.product.change-status}")
    private String productChangeStatusTopic;

    @Value("${spring.kafka.topic.user.change-status}")
    private String userChangeStatusTopic;


    @Bean
    public NewTopic organizationRegisterEventsTopic() {
        return TopicBuilder
                .name(organizationRegisterTopic)
                .build();
    }

    @Bean
    public NewTopic productRegisterEventsTopic() {
        return TopicBuilder
                .name(productRegisterTopic)
                .build();
    }

    @Bean
    public NewTopic adminEventsTopic() {
        return TopicBuilder
                .name(adminTopicName)
                .build();
    }

    @Bean
    public NewTopic organizationChangeStatusEventsTopic() {
        return TopicBuilder
                .name(organizationChangeStatusTopic)
                .build();
    }

    @Bean
    public NewTopic productChangeStatusEventsTopic() {
        return TopicBuilder
                .name(productChangeStatusTopic)
                .build();
    }

    @Bean
    public NewTopic userChangeStatusEventsTopic() {
        return TopicBuilder
                .name(userChangeStatusTopic)
                .build();
    }
}

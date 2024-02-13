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

    @Value("${spring.kafka.topic.organization.freeze}")
    private String organizationFreezeTopic;

    @Value("${spring.kafka.topic.product.freeze}")
    private String productFreezeTopic;

    @Value("${spring.kafka.topic.user.freeze}")
    private String userFreezeTopic;


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
    public NewTopic organizationFreezeEventsTopic() {
        return TopicBuilder
                .name(organizationFreezeTopic)
                .build();
    }

    @Bean
    public NewTopic productFreezeEventsTopic() {
        return TopicBuilder
                .name(productFreezeTopic)
                .build();
    }

    @Bean
    public NewTopic userFreezeEventsTopic() {
        return TopicBuilder
                .name(userFreezeTopic)
                .build();
    }
}

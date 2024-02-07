package com.ryazancev.organization.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class OrganizationTopicConfig {

    @Value("${spring.kafka.topic.admin}")
    private String adminTopicName;

    @Value("${spring.kafka.topic.organization.register}")
    private String organizationRegisterTopicName;


    @Bean
    public NewTopic adminEventsTopic() {
        return TopicBuilder
                .name(adminTopicName)
                .build();
    }

    @Bean
    public NewTopic organizationRegisterEventsTopic() {
        return TopicBuilder
                .name(organizationRegisterTopicName)
                .build();
    }
}

package com.ryazancev.organization.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author Oleg Ryazancev
 */

@Configuration
public class OrganizationTopicConfig {

    @Value("${spring.kafka.topic.admin}")
    private String adminTopicName;

    @Value("${spring.kafka.topic.organization.register}")
    private String organizationRegisterTopicName;

    @Value("${spring.kafka.topic.product}")
    private String productDeleteTopicName;

    @Value("${spring.kafka.topic.organization.change-status}")
    private String organizationChangeStatusTopicName;


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

    @Bean
    public NewTopic productDeleteEventsTopic() {
        return TopicBuilder
                .name(productDeleteTopicName)
                .build();
    }

    @Bean
    public NewTopic productChangeStatusEventsTopic() {
        return TopicBuilder
                .name(organizationChangeStatusTopicName)
                .build();
    }
}

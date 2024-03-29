package com.ryazancev.customer.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author Oleg Ryazancev
 */

@Configuration
public class CustomerTopicConfig {

    @Value("${spring.kafka.topic.user.delete}")
    private String userDeleteTopicName;


    @Value("${spring.kafka.topic.customer.update}")
    private String customerUpdateTopicName;

    @Value("${spring.kafka.topic.user.update}")
    private String userUpdateTopicName;


    @Value("${spring.kafka.topic.notification}")
    private String notificationTopicName;


    @Bean
    public NewTopic customerUpdateEventsTopic() {
        return TopicBuilder
                .name(customerUpdateTopicName)
                .build();
    }

    @Bean
    public NewTopic userDeleteEventsTopic() {
        return TopicBuilder
                .name(userDeleteTopicName)
                .build();
    }

    @Bean
    public NewTopic userUpdateEventsTopic() {
        return TopicBuilder
                .name(userUpdateTopicName)
                .build();
    }

    @Bean
    public NewTopic notificationEventsTopic() {
        return TopicBuilder
                .name(notificationTopicName)
                .build();
    }
}

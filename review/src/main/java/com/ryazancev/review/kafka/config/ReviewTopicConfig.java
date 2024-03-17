package com.ryazancev.review.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author Oleg Ryazancev
 */

@Configuration
public class ReviewTopicConfig {

    @Value("${spring.kafka.topic.notification}")
    private String notificationTopic;

    @Bean
    public NewTopic notificationEventsTopic() {
        return TopicBuilder
                .name(notificationTopic)
                .build();
    }
}

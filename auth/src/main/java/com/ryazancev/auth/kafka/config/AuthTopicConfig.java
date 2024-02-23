package com.ryazancev.auth.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AuthTopicConfig {

    @Value("${spring.kafka.topic.mail}")
    private String emailTopic;

    @Value("${spring.kafka.topic.user.toggle-lock}")
    private String userToggleLockTopic;

    @Value("${spring.kafka.topic.user.update}")
    private String userUpdateTopic;


    @Bean
    public NewTopic emailEventsTopic() {
        return TopicBuilder
                .name(emailTopic)
                .build();
    }

    @Bean
    public NewTopic userToggleLockEventsTopic() {
        return TopicBuilder
                .name(userToggleLockTopic)
                .build();
    }

    @Bean
    public NewTopic userUpdateEventsTopic() {
        return TopicBuilder
                .name(userUpdateTopic)
                .build();
    }
}

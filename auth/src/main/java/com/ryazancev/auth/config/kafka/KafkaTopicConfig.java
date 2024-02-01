package com.ryazancev.auth.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic}")
    private String topicName;


    @Bean
    public NewTopic emailEventsTopic() {
        return TopicBuilder
                .name(topicName)
                .build();
    }
}

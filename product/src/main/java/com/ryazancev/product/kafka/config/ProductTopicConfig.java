package com.ryazancev.product.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author Oleg Ryazancev
 */

@Configuration
public class ProductTopicConfig {

    @Value("${spring.kafka.topic.admin}")
    private String adminTopicName;

    @Value("${spring.kafka.topic.product.register}")
    private String registerProductTopicName;

    @Value("${spring.kafka.topic.product.update}")
    private String updateProductTopicName;

    @Value("${spring.kafka.topic.product.change-status}")
    private String changeStatusProductTopicName;

    @Value("${spring.kafka.topic.review}")
    private String deleteReviewTopicName;

    @Value("${spring.kafka.topic.mail}")
    private String mailTopicName;


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

    @Bean
    public NewTopic freezeProductTopic() {

        return TopicBuilder
                .name(changeStatusProductTopicName)
                .build();
    }

    @Bean
    public NewTopic mailEventsTopic() {

        return TopicBuilder
                .name(mailTopicName)
                .build();
    }


    @Bean
    public NewTopic deleteReviewTopic() {

        return TopicBuilder
                .name(deleteReviewTopicName)
                .build();
    }
}

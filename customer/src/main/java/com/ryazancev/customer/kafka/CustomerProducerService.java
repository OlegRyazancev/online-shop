package com.ryazancev.customer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerProducerService {

    private final KafkaTemplate<String, Long> longValueKafkaTemplate;

    @Value("${spring.kafka.topic.user.delete}")
    private String deleteUserTopic;

    public CustomerProducerService(
            @Qualifier("longValueKafkaTemplate")
            KafkaTemplate<String, Long> longValueKafkaTemplate) {

        this.longValueKafkaTemplate = longValueKafkaTemplate;
    }

    public void sendMessageToAuthTopic(Long id) {

        try {
            longValueKafkaTemplate.send(deleteUserTopic, id);

            log.info("Message to auth service to delete user " +
                    "with customer id: {} was successfully sent", id);

        } catch (Exception e) {
            log.info("Message to auth service to delete user " +
                    "with id: {} was not sent", id);
        }
    }
}

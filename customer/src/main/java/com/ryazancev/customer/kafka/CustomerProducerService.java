package com.ryazancev.customer.kafka;

import com.ryazancev.common.dto.user.UserUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Oleg Ryazancev
 */

@Service
@Slf4j
public class CustomerProducerService {

    private final KafkaTemplate<
            String, Long> longValueKafkaTemplate;

    private final KafkaTemplate<
            String, UserUpdateRequest> userUpdateKafkaTemplate;

    @Value("${spring.kafka.topic.user.delete}")
    private String deleteUserTopic;

    @Value("${spring.kafka.topic.user.update}")
    private String updateUserTopic;

    public CustomerProducerService(
            @Qualifier("longValueKafkaTemplate")
            KafkaTemplate<String, Long> longValueKafkaTemplate,
            @Qualifier("userUpdateKafkaTemplate")
            KafkaTemplate<String, UserUpdateRequest> userUpdateKafkaTemplate) {

        this.longValueKafkaTemplate = longValueKafkaTemplate;
        this.userUpdateKafkaTemplate = userUpdateKafkaTemplate;
    }

    public void sendMessageToAuthDeleteTopic(Long id) {

        log.info("Received request to delete user with id {} " +
                "in the auth topic", id);

        try {

            log.debug("Sending delete user request...");
            longValueKafkaTemplate.send(deleteUserTopic, id);

            log.debug("Delete user request sent to topic: {}", deleteUserTopic);

        } catch (Exception e) {

            log.error("Failed to send delete user request to {}: {}",
                    deleteUserTopic,
                    e.getMessage());
        }
    }

    public void sendMessageToAuthUpdateTopic(UserUpdateRequest request) {

        log.info("Received request to update user with id: {}, email: {}, " +
                        "name: {} in the auth topic",
                request.getCustomerId(),
                request.getEmail(),
                request.getName());

        try {

            log.debug("Sending update user request...");
            userUpdateKafkaTemplate.send(updateUserTopic, request);

            log.debug("Update user request sent to topic: {}",
                    updateUserTopic);

        } catch (Exception e) {

            log.error("Failed to send update user request to {}: {}",
                    updateUserTopic,
                    e.getMessage());
        }
    }
}

package com.ryazancev.customer.kafka;

import com.ryazancev.common.dto.user.UserUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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

        try {
            longValueKafkaTemplate.send(deleteUserTopic, id);

            log.info("Message to auth service to delete user " +
                    "with customer id: {} was successfully sent", id);

        } catch (Exception e) {
            log.info("Message to auth service to delete user " +
                    "with id: {} was not sent", id);
        }
    }

    public void sendMessageToAuthUpdateTopic(UserUpdateRequest request) {

        try {
            userUpdateKafkaTemplate.send(updateUserTopic, request);

            log.info("Message to auth service to update user by customer id " +
                            "with customer id: {} was successfully sent",
                    request.getCustomerId());


        } catch (Exception e) {
            log.info("Message to auth service to update user by customer id" +
                            "with id: {} was not sent",
                    request.getCustomerId());
        }
    }
}

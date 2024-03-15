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

        log.info("Request to auth topic to delete user with id {} was" +
                " received", id);
        try {

            log.trace("sending request...}");
            longValueKafkaTemplate.send(deleteUserTopic, id);

            log.debug("Request to {} was send", deleteUserTopic);

        } catch (Exception e) {

            log.error("Request to {} was not send",
                    deleteUserTopic);
        }
    }

    public void sendMessageToAuthUpdateTopic(UserUpdateRequest request) {

        log.info("Request to auth topic to update user " +
                        "with id:{}, email:{}, name: {} was received",
                request.getCustomerId(),
                request.getEmail(),
                request.getName());

        try {

            log.trace("sending request...}");
            userUpdateKafkaTemplate.send(updateUserTopic, request);

            log.debug("Request to {} was send", updateUserTopic);

        } catch (Exception e) {

            log.error("Request to {} was not send",
                    updateUserTopic);
        }
    }
}

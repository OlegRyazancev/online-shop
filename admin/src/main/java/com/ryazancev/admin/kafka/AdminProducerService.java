package com.ryazancev.admin.kafka;


import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.util.mapper.AdminMapper;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.UserLockRequest;
import com.ryazancev.common.dto.notification.NotificationRequest;
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
public class AdminProducerService {

    private final KafkaTemplate<String,
            RegistrationRequestDto> registerKafkaTemplate;
    private final KafkaTemplate<String,
            ObjectRequest> changeStatusKafkaTemplate;

    private final KafkaTemplate<String,
            UserLockRequest> toggleUserLockKafkaTemplate;

    private final KafkaTemplate<String,
            NotificationRequest> notificationKafkaTemplate;
    private final AdminMapper adminMapper;

    @Value("${spring.kafka.topic.organization.register}")
    private String organizationRegisterTopic;

    @Value("${spring.kafka.topic.product.register}")
    private String productRegisterTopic;

    @Value("${spring.kafka.topic.organization.change-status}")
    private String organizationChangeStatusTopic;

    @Value("${spring.kafka.topic.product.change-status}")
    private String productChangeStatusTopic;

    @Value("${spring.kafka.topic.user.toggle-lock}")
    private String toggleUserLockTopic;

    @Value("${spring.kafka.topic.notification}")
    private String notificationTopic;

    public AdminProducerService(
            @Qualifier("registerKafkaTemplate")
            KafkaTemplate<String, RegistrationRequestDto>
                    registerKafkaTemplate,
            @Qualifier("changeStatusKafkaTemplate")
            KafkaTemplate<String, ObjectRequest>
                    changeStatusKafkaTemplate,
            @Qualifier("toggleUserLockKafkaTemplate")
            KafkaTemplate<String, UserLockRequest>
                    toggleUserLockKafkaTemplate,
            @Qualifier("notificationKafkaTemplate")
            KafkaTemplate<String, NotificationRequest>
                    notificationKafkaTemplate,
            AdminMapper adminMapper) {

        this.registerKafkaTemplate = registerKafkaTemplate;
        this.changeStatusKafkaTemplate = changeStatusKafkaTemplate;
        this.toggleUserLockKafkaTemplate = toggleUserLockKafkaTemplate;
        this.notificationKafkaTemplate = notificationKafkaTemplate;
        this.adminMapper = adminMapper;
    }

    public void sendRegisterResponse(RegistrationRequest request) {

        RegistrationRequestDto requestDto =
                adminMapper.toDto(request);

        log.info("Received request to send register" +
                        " response of {} with status: {} with id: {}",
                requestDto.getObjectType(),
                requestDto.getStatus(),
                requestDto.getObjectToRegisterId());

        try {

            String topic;

            switch (requestDto.getObjectType()) {

                case PRODUCT -> {

                    topic = productRegisterTopic;
                }
                case ORGANIZATION -> {

                    topic = organizationRegisterTopic;
                }
                default -> {
                    log.warn("Unknown request/object type: {}",
                            requestDto.getObjectType());
                    return;
                }
            }

            log.trace("Sending register response request...");
            registerKafkaTemplate.send(topic, requestDto);

            log.debug("Register response request sent to topic: {}", topic);

        } catch (Exception e) {

            log.error("Failed to send register response of {} with id: {}: {}",
                    requestDto.getObjectType(),
                    requestDto.getObjectToRegisterId(),
                    e.getMessage());
        }
    }

    public void sendMessageToChangeObjectStatus(ObjectRequest objectRequest) {

        log.info("Received request to change {} status to {} with " +
                        "object id: {} was received",
                objectRequest.getObjectType(),
                objectRequest.getObjectStatus(),
                objectRequest.getObjectId());

        try {

            String topic;

            switch (objectRequest.getObjectType()) {

                case PRODUCT -> {

                    topic = productChangeStatusTopic;
                }
                case ORGANIZATION -> {

                    topic = organizationChangeStatusTopic;
                }
                default -> {
                    log.warn("Unknown request/object type: {}",
                            objectRequest.getObjectType());
                    return;
                }
            }

            log.trace("Sending change object status request...");
            changeStatusKafkaTemplate.send(topic, objectRequest);

            log.debug("Change object status request sent to topic: {}", topic);

        } catch (Exception e) {

            log.error("Request to change {} status was not sent: {}",
                    objectRequest.getObjectType(),
                    e.getMessage());
        }
    }

    public void sendMessageToToggleUserLock(UserLockRequest request) {

        log.info("Received request to toggle user by id: {} " +
                        "lock {} was received",
                request.getUserId(),
                request.isLock());

        try {

            log.trace("Sending toggle user lock request...");
            toggleUserLockKafkaTemplate.send(toggleUserLockTopic, request);

            log.debug("Toggle user lock request sent to topic: {}",
                    toggleUserLockTopic);

        } catch (Exception e) {

            log.error("Failed to send toggle user lock request: {}",
                    e.getMessage());
        }
    }

    public void sendNotification(NotificationRequest request) {

        log.info("Received request to send {} notification {} to user " +
                        "with id: {} from admin if exists: {}",
                request.getScope(),
                request.getType(),
                request.getRecipientId(),
                request.getSenderId());

        try {

            log.trace("Sending notification request...");
            notificationKafkaTemplate.send(notificationTopic, request);

            log.debug("Notification request sent to topic: {}",
                    notificationTopic);

        } catch (Exception e) {

            log.error("Failed to send notification request: {}",
                    e.getMessage());
        }
    }
}

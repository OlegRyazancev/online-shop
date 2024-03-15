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

        //todo: change here
        RegistrationRequestDto requestDto =
                adminMapper.toDto(request);

        log.info("Request to send register response of {} with status: {} " +
                        "with id: {} was received",
                requestDto.getObjectType(),
                requestDto.getStatus(),
                request.getObjectToRegisterId());
        try {
            switch (requestDto.getObjectType()) {

                case PRODUCT -> {

                    log.trace("sending request...}");
                    registerKafkaTemplate.send(
                            productRegisterTopic, requestDto);

                    log.debug("Request to {} was sent",
                            productRegisterTopic);
                }
                case ORGANIZATION -> {

                    log.trace("sending request...}");
                    registerKafkaTemplate.send(
                            organizationRegisterTopic, requestDto);

                    log.debug("Request to {} was sent",
                            organizationRegisterTopic);
                }
                default -> {
                    log.warn("Unknown request/object type: {}",
                            requestDto.getObjectType());
                }
            }
        } catch (Exception e) {

            log.error("Request to send register response of {} with id:" +
                            " {} was not sent: {}",
                    requestDto.getObjectType(),
                    requestDto.getObjectToRegisterId(),
                    e.getMessage());
        }
    }

    public void sendMessageToChangeObjectStatus(ObjectRequest objectRequest) {

        log.info("Request to change {} status to {} with object " +
                        "id: {} was received",
                objectRequest.getObjectType(),
                objectRequest.getObjectStatus(),
                objectRequest.getObjectId());

        try {
            switch (objectRequest.getObjectType()) {
                case PRODUCT -> {

                    log.trace("sending request...}");
                    changeStatusKafkaTemplate.send(
                            productChangeStatusTopic, objectRequest);

                    log.debug("Request to {} was sent",
                            productChangeStatusTopic);
                }
                case ORGANIZATION -> {

                    log.trace("sending request...}");
                    changeStatusKafkaTemplate.send(
                            organizationChangeStatusTopic, objectRequest);

                    log.debug("Request to {} was sent",
                            organizationChangeStatusTopic);
                }
                default -> {
                    log.warn("Unknown request/object type: {}",
                            objectRequest.getObjectType());
                }
            }
        } catch (Exception e) {

            log.error("Request to change {} status was not sent: {}",
                    objectRequest.getObjectType(),
                    e.getMessage());
        }
    }

    public void sendMessageToToggleUserLock(UserLockRequest request) {

        log.info("Request to toggle user by id: {} lock {} " +
                        "was received",
                request.getUserId(),
                request.isLock());
        try {

            log.trace("sending request...}");
            toggleUserLockKafkaTemplate.send(toggleUserLockTopic, request);

            log.debug("Request to {} was sent",
                    toggleUserLockTopic);

        } catch (Exception e) {

            log.error("Request to {} was not sent: {}",
                    toggleUserLockTopic,
                    e.getMessage());
        }
    }

    public void sendNotification(NotificationRequest request) {

        log.info("Request to send {} notification {} to user with id: " +
                        "{} from admin if exists: {} was received",
                request.getScope(),
                request.getType(),
                request.getRecipientId(),
                request.getSenderId());
        try {

            log.trace("sending request...}");
            notificationKafkaTemplate.send(notificationTopic, request);

            log.debug("Request to {} was sent",
                    notificationTopic);

        } catch (Exception e) {

            log.error("Request to {} was not sent: {}",
                    notificationTopic,
                    e.getMessage());
        }
    }
}

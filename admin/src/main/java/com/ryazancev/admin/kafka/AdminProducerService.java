package com.ryazancev.admin.kafka;


import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.util.mapper.AdminMapper;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.UserLockRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminProducerService {

    private final KafkaTemplate<String,
            RegistrationRequestDto> registerKafkaTemplate;
    private final KafkaTemplate<String,
            ObjectRequest> changeStatusKafkaTemplate;

    private final KafkaTemplate<String,
            UserLockRequest> toggleUserLockKafkaTemplate;

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

    public AdminProducerService(
            @Qualifier("registerKafkaTemplate")
            KafkaTemplate<String, RegistrationRequestDto> registerKafkaTemplate,
            @Qualifier("changeStatusKafkaTemplate")
            KafkaTemplate<String, ObjectRequest> changeStatusKafkaTemplate,
            @Qualifier("toggleUserLockKafkaTemplate")
            KafkaTemplate<String, UserLockRequest> toggleUserLockKafkaTemplate,
            AdminMapper adminMapper) {

        this.registerKafkaTemplate = registerKafkaTemplate;
        this.changeStatusKafkaTemplate = changeStatusKafkaTemplate;
        this.toggleUserLockKafkaTemplate = toggleUserLockKafkaTemplate;
        this.adminMapper = adminMapper;
    }

    public void sendRegisterResponse(RegistrationRequest request) {
        RegistrationRequestDto requestDto =
                adminMapper.toDto(request);

        switch (requestDto.getObjectType()) {

            case PRODUCT -> {

                try {
                    registerKafkaTemplate.send(
                            productRegisterTopic,
                            requestDto);

                    log.info(
                            "Request sent to product topic with: {} and status {}",
                            requestDto.getObjectType(),
                            requestDto.getStatus()
                    );
                } catch (Exception e) {
                    log.info("Request to product topic with: {} and " +
                                    "status {} was not sent",
                            requestDto.getObjectType(),
                            requestDto.getStatus());
                }

            }
            case ORGANIZATION -> {

                try {
                    registerKafkaTemplate.send(
                            organizationRegisterTopic,
                            requestDto);

                    log.info("Request sent to organization topic with: " +
                                    "{} and status {}",
                            requestDto.getObjectType(),
                            requestDto.getStatus());
                } catch (Exception e) {
                    log.info("Request to organization topic with: {} and " +
                                    "status {} was not sent",
                            requestDto.getObjectType(),
                            requestDto.getStatus());
                }

            }
            default -> {
                log.info("Unknown request/object type: {}",
                        requestDto.getObjectType());
            }
        }
    }

    public void sendMessageToChangeObjectStatus(ObjectRequest objectRequest) {

        switch (objectRequest.getObjectType()) {
            case PRODUCT -> {

                try {
                    changeStatusKafkaTemplate.send(
                            productChangeStatusTopic, objectRequest);

                    log.info("Request to change product status to " +
                                    "{} with id: {} successfully sent",
                            objectRequest.getObjectStatus(),
                            objectRequest.getObjectId());
                } catch (Exception e) {
                    log.info("Request to change product status " +
                            "was not successfully sent");
                }

            }
            case ORGANIZATION -> {

                try {
                    changeStatusKafkaTemplate.send(
                            organizationChangeStatusTopic, objectRequest);

                    log.info("Request to change organization status to " +
                                    "{} with id: {} successfully sent",
                            objectRequest.getObjectStatus(),
                            objectRequest.getObjectId());
                } catch (Exception e) {
                    log.info("Request to change organization status " +
                            "was not successfully sent");
                }

            }
            default -> {
                log.info("Unknown request/object type: {}",
                        objectRequest.getObjectType());
            }
        }
    }

    public void sendMessageToToggleUserLock(UserLockRequest request) {

        try {
            toggleUserLockKafkaTemplate.send(toggleUserLockTopic, request);

            log.info("Request to toggle user: {} lock {} " +
                            "was successfully sent",
                    request.getUsername(),
                    request.isLock());
        } catch (Exception e) {
            log.info("Request to toggle user: {} lock {} " +
                            "was not successfully sent",
                    request.getUsername(),
                    request.isLock());
        }
    }
}

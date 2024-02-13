package com.ryazancev.admin.kafka;


import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.util.mapper.AdminMapper;
import com.ryazancev.dto.admin.ObjectRequest;
import com.ryazancev.dto.admin.RegistrationRequestDto;
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
    private final KafkaTemplate<String, ObjectRequest> changeStatusKafkaTemplate;

    private final AdminMapper adminMapper;

    @Value("${spring.kafka.topic.organization.register}")
    private String organizationRegisterTopic;


    @Value("${spring.kafka.topic.product.register}")
    private String productRegisterTopic;

    @Value("${spring.kafka.topic.organization.change-status}")
    private String organizationChangeStatusTopic;

    @Value("${spring.kafka.topic.product.change-status}")
    private String productChangeStatusTopic;

    @Value("${spring.kafka.topic.user.change-status}")
    private String userChangeStatusTopic;

    public AdminProducerService(
            @Qualifier("registerKafkaTemplate")
            KafkaTemplate<String, RegistrationRequestDto> registerKafkaTemplate,
            @Qualifier("changeStatusKafkaTemplate")
            KafkaTemplate<String, ObjectRequest> changeStatusKafkaTemplate,
            AdminMapper adminMapper) {

        this.registerKafkaTemplate = registerKafkaTemplate;
        this.changeStatusKafkaTemplate = changeStatusKafkaTemplate;
        this.adminMapper = adminMapper;
    }

    public void sendRegisterResponse(RegistrationRequest request) {
        RegistrationRequestDto requestDTO =
                adminMapper.toDto(request);

        switch (requestDTO.getObjectType()) {

            case PRODUCT -> {
                registerKafkaTemplate.send(productRegisterTopic, requestDTO);
                log.info(
                        "Request sent to product topic with: {} and status {}",
                        requestDTO.getObjectType(),
                        requestDTO.getStatus()
                );
            }
            case ORGANIZATION -> {
                registerKafkaTemplate.send(organizationRegisterTopic, requestDTO);
                log.info("Request sent to organization topic with: " +
                                "{} and status {}",
                        requestDTO.getObjectType(),
                        requestDTO.getStatus());
            }
            default -> {
                log.info("Unknown request/object type: {}",
                        requestDTO.getObjectType());
            }
        }
    }

    public void sendMessageToChangeObjectStatus(ObjectRequest objectRequest) {

        switch (objectRequest.getObjectType()) {
            case PRODUCT -> {
                changeStatusKafkaTemplate.send(
                        productChangeStatusTopic, objectRequest);

                log.info("Request to change product status to " +
                                "{} with id: {} successfully sent",
                        objectRequest.getObjectStatus(),
                        objectRequest.getObjectId());
            }
            case ORGANIZATION -> {
                changeStatusKafkaTemplate.send(
                        organizationChangeStatusTopic, objectRequest);

                log.info("Request to change organization status to " +
                                "{} with id: {} successfully sent",
                        objectRequest.getObjectStatus(),
                        objectRequest.getObjectId());
            }
            case USER -> {
                changeStatusKafkaTemplate.send(
                        userChangeStatusTopic, objectRequest);

                log.info("Request to change user status to " +
                                "{} with id: {} successfully sent",
                        objectRequest.getObjectStatus(),
                        objectRequest.getObjectId());
            }
            default -> {
                log.info("Unknown request/object type: {}",
                        objectRequest.getObjectType());
            }
        }
    }
}

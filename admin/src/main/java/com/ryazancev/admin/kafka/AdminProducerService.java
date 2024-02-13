package com.ryazancev.admin.kafka;


import com.ryazancev.admin.dto.FreezeRequest;
import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.util.mapper.AdminMapper;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminProducerService {

    private final KafkaTemplate<String,
            RegistrationRequestDTO> registerKafkaTemplate;
    private final KafkaTemplate<String, Long> freezeKafkaTemplate;

    private final AdminMapper adminMapper;

    @Value("${spring.kafka.topic.organization.register}")
    private String organizationRegisterTopic;


    @Value("${spring.kafka.topic.product.register}")
    private String productRegisterTopic;

    @Value("${spring.kafka.topic.organization.freeze}")
    private String organizationFreezeTopic;

    @Value("${spring.kafka.topic.product.freeze}")
    private String productFreezeTopic;

    @Value("${spring.kafka.topic.user.freeze}")
    private String userFreezeTopic;

    public AdminProducerService(
            @Qualifier("registerKafkaTemplate")
            KafkaTemplate<String, RegistrationRequestDTO> registerKafkaTemplate,
            @Qualifier("freezeKafkaTemplate")
            KafkaTemplate<String, Long> freezeKafkaTemplate,
            AdminMapper adminMapper) {

        this.registerKafkaTemplate = registerKafkaTemplate;
        this.freezeKafkaTemplate = freezeKafkaTemplate;
        this.adminMapper = adminMapper;
    }

    public void sendRegisterResponse(RegistrationRequest request) {
        RegistrationRequestDTO requestDTO =
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

    public void sendMessageToFreezeObject(FreezeRequest freezeRequest) {

        switch (freezeRequest.getObjectType()) {
            case PRODUCT -> {
                log.info("Request to freeze product with id: {} " +
                                "successfully sent",
                        freezeRequest.getObjectId());

                freezeKafkaTemplate.send(
                        productFreezeTopic, freezeRequest.getObjectId());
            }
            case ORGANIZATION -> {
                freezeKafkaTemplate.send(
                        organizationFreezeTopic, freezeRequest.getObjectId());

                log.info("Request to freeze organization with id: {} " +
                                "successfully sent",
                        freezeRequest.getObjectId());
            }
            case USER -> {
                freezeKafkaTemplate.send(
                        userFreezeTopic, freezeRequest.getObjectId());

                log.info("Request to freeze user with id: {} " +
                                "successfully sent",
                        freezeRequest.getObjectId());
            }
            default -> {
                log.info("Unknown request/object type: {}",
                        freezeRequest.getObjectType());
            }
        }
    }
}

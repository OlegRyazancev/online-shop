package com.ryazancev.admin.kafka;


import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.util.mapper.RegistrationRequestMapper;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminProducerService {

    @Value("${spring.kafka.topic.organization.register}")
    private String organizationTopic;

    @Value("${spring.kafka.topic.product.register}")
    private String productTopic;

    private final KafkaTemplate<String, RegistrationRequestDTO> kafkaTemplate;
    private final RegistrationRequestMapper registrationRequestMapper;

    public void sendResponse(RegistrationRequest request) {
        RegistrationRequestDTO requestDTO =
                registrationRequestMapper.toDto(request);

        switch (requestDTO.getObjectType()) {

            case PRODUCT->{
                kafkaTemplate.send(productTopic, requestDTO);
                log.info(
                        "Request sent to product topic with: {} and status {}",
                        requestDTO.getObjectType(),
                        requestDTO.getStatus()
                );
            }
            case ORGANIZATION->{
                kafkaTemplate.send(organizationTopic, requestDTO);
                log.info("Request sent to organization topic with: " +
                                "{} and status {}",
                        requestDTO.getObjectType(),
                        requestDTO.getStatus());
            }
            default->{
                log.info("Unknown request/object type: {}",
                        requestDTO.getObjectType());
            }
        }
    }
}

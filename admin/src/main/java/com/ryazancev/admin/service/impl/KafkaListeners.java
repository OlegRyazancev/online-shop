package com.ryazancev.admin.service.impl;

import com.ryazancev.admin.service.OrgRegRequestService;
import com.ryazancev.dto.admin.RegistrationRequest;
import com.ryazancev.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {


    private final OrgRegRequestService orgRegRequestService;

    @KafkaListener(
            topics = "${spring.kafka.topic.admin}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void createRegistrationRequest(RegistrationRequest request) {
        log.info("Received message to register organization/product with id {}",
                request.getObjectToBeRegisteredId());

        log.info("Creating request...");
        switch (request.getType()) {

            case ORGANIZATION -> {

                orgRegRequestService.create(
                        request.getObjectToBeRegisteredId());
                log.info("Request for organization created successfully!");
            }

            case PRODUCT -> {

            }
            default -> {
            }
        }


    }
}

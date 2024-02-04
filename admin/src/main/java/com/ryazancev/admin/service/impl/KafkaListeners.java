package com.ryazancev.admin.service.impl;

import com.ryazancev.admin.service.RegistrationRequestService;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {


    private final RegistrationRequestService registrationRequestService;

    @KafkaListener(
            topics = "${spring.kafka.topic.admin}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void createRegistrationRequest(RegistrationRequestDTO request) {

        log.info("Received message to register organization/product with id {}," +
                        "and type: {}",
                request.getObjectToRegisterId(),
                request.getObjectType().name());

        log.info("Creating request...");

        registrationRequestService.create(request);

        log.info("Request successfully created");
    }
}


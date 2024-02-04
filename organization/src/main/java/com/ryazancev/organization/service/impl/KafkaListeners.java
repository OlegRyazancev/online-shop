package com.ryazancev.organization.service.impl;

import com.ryazancev.dto.admin.RegistrationResponse;
import com.ryazancev.dto.admin.ResponseStatus;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {


    private final OrganizationService organizationService;

    @KafkaListener(
            topics = "${spring.kafka.topic.admin}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void completeRegistrationOfOrganization(RegistrationResponse response) {
        log.info("Received answer message from admin with response {} ",
                response.getStatus().name());

        switch (response.getStatus()) {
            case ACCEPTED -> {
                organizationService.changeStatusAndRegister(
                        response.getObjectToBeRegisteredId(),
                        OrganizationStatus.ACTIVE);

                log.info("Organization now is: {}",
                        OrganizationStatus.ACTIVE);
            }
            case REJECTED -> {
                organizationService.changeStatusAndRegister(
                        response.getObjectToBeRegisteredId(),
                        OrganizationStatus.INACTIVE);

                log.info("Organization now is: {}",
                        OrganizationStatus.INACTIVE);
            }
            default -> {
            }
        }


        log.info("Creating request...");


    }
}

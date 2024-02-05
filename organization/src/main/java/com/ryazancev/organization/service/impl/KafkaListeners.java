package com.ryazancev.organization.service.impl;

import com.ryazancev.dto.admin.RegistrationRequestDTO;
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
            topics = "${spring.kafka.topic.organization.register}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void completeRegistrationOfOrganization(RegistrationRequestDTO requestDTO) {

        log.info("Received answer message from admin with response {} ",
                requestDTO.getStatus().name());

        switch (requestDTO.getStatus()) {
            case ACCEPTED -> {
                organizationService.changeStatusAndRegister(
                        requestDTO.getObjectToRegisterId(),
                        OrganizationStatus.ACTIVE);

                log.info("Organization now is: {}",
                        OrganizationStatus.ACTIVE);
            }
            case REJECTED -> {
                organizationService.changeStatusAndRegister(
                        requestDTO.getObjectToRegisterId(),
                        OrganizationStatus.INACTIVE);

                log.info("Organization now is: {}",
                        OrganizationStatus.INACTIVE);
            }
            default -> {
            }
        }
    }
}

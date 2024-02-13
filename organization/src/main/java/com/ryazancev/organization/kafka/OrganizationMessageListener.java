package com.ryazancev.organization.kafka;

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
public class OrganizationMessageListener {


    private final OrganizationService organizationService;

    @KafkaListener(
            topics = "${spring.kafka.topic.organization.register}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "registrationMessageFactory"
    )
    void completeRegistrationOfOrganization(RegistrationRequestDTO requestDTO) {

        log.info("Received answer message from admin with response {} ",
                requestDTO.getStatus().name());

        switch (requestDTO.getStatus()) {
            case ACCEPTED -> {
                organizationService.changeStatus(
                        requestDTO.getObjectToRegisterId(),
                        OrganizationStatus.ACTIVE);
                organizationService.register(
                        requestDTO.getObjectToRegisterId()
                );

                log.info("Organization now is: {}",
                        OrganizationStatus.ACTIVE);
            }
            case REJECTED -> {
                organizationService.changeStatus(
                        requestDTO.getObjectToRegisterId(),
                        OrganizationStatus.INACTIVE);

                log.info("Organization now is: {}",
                        OrganizationStatus.INACTIVE);
            }
            default -> {
                log.info("Unknown request type({}) or status({})",
                        requestDTO.getObjectType(),
                        requestDTO.getStatus());
            }
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.organization.freeze}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "freezeMessageFactory"
    )
    public void freezeOrganization(Long organizationId) {

        log.info("Received message from admin to freeze " +
                        "organization with id: {}",
                organizationId);

        organizationService.changeStatus(
                organizationId,
                OrganizationStatus.FROZEN);

        log.info("Organization successfully froze");
    }
}

package com.ryazancev.organization.kafka;

import com.ryazancev.dto.admin.ObjectRequest;
import com.ryazancev.dto.admin.RegistrationRequestDto;
import com.ryazancev.dto.admin.enums.ObjectStatus;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.OrganizationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrganizationMessageListener {


    private final OrganizationService organizationService;
    private final OrganizationUtil organizationUtil;

    @KafkaListener(
            topics = "${spring.kafka.topic.organization.register}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "registrationMessageFactory"
    )
    void completeRegistrationOfOrganization(RegistrationRequestDto requestDto) {

        log.info("Received answer message from admin with response {} ",
                requestDto.getStatus().name());

        switch (requestDto.getStatus()) {
            case ACCEPTED -> {

                organizationService.changeStatus(
                        requestDto.getObjectToRegisterId(),
                        OrganizationStatus.ACTIVE);
                organizationService.register(
                        requestDto.getObjectToRegisterId()
                );

                try {
                    organizationUtil.sendAcceptedMailToCustomerByOrganizationId(
                            requestDto.getObjectToRegisterId());
                } catch (Exception e) {
                    log.error("Error during sending email to customer: {}",
                            e.getMessage());
                }

                log.info("Organization now is: {}",
                        OrganizationStatus.ACTIVE);
            }
            case REJECTED -> {
                organizationService.changeStatus(
                        requestDto.getObjectToRegisterId(),
                        OrganizationStatus.INACTIVE);
                try {
                    organizationUtil.sendRejectedMailToCustomerByOrganizationId(
                            requestDto.getObjectToRegisterId());
                } catch (Exception e) {
                    log.error("Error during sending email to customer: {}",
                            e.getMessage());
                }

                log.info("Organization now is: {}",
                        OrganizationStatus.INACTIVE);
            }
            default -> {
                log.info("Unknown request type({}) or status({})",
                        requestDto.getObjectType(),
                        requestDto.getStatus());
            }
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.organization.change-status}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "changeStatusMessageFactory"
    )
    public void changeOrganizationStatus(ObjectRequest request) {

        log.info("Received message from admin to change status of " +
                        "organization with id: {}, to {}",
                request.getObjectId(),
                request.getObjectStatus());

        OrganizationStatus status =
                (request.getObjectStatus() == ObjectStatus.ACTIVATE)
                        ? OrganizationStatus.ACTIVE
                        : OrganizationStatus.FROZEN;

        organizationService.changeStatus(request.getObjectId(), status);

        log.info("Organization status successfully changed to: {}", status);
    }
}

package com.ryazancev.organization.kafka;

import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.enums.ObjectStatus;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.MailProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OrganizationMessageListener {


    private final OrganizationService organizationService;
    private final MailProcessor mailProcessor;

    @KafkaListener(
            topics = "${spring.kafka.topic.organization.register}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "registrationMessageFactory"
    )
    void changeStatusAndRegister(RegistrationRequestDto requestDto) {

        log.info("Received message from admin to register organization with" +
                        " id {} with response: {}",
                requestDto.getObjectToRegisterId(),
                requestDto.getStatus());

        try {

            switch (requestDto.getStatus()) {
                case ACCEPTED -> {

                    log.trace("Changing status to ACTIVE...");
                    organizationService.changeStatus(
                            requestDto.getObjectToRegisterId(),
                            OrganizationStatus.ACTIVE);

                    log.trace("Registering organization...");
                    organizationService.register(
                            requestDto.getObjectToRegisterId()
                    );

                    log.trace("Sending accepted email...");
                    mailProcessor.sendAcceptedMailToCustomerByOrganizationId(
                            requestDto.getObjectToRegisterId());

                    log.debug("Organization status is now: {}",
                            OrganizationStatus.ACTIVE);
                }
                case REJECTED -> {

                    log.trace("Changing status to INACTIVE..");
                    organizationService.changeStatus(
                            requestDto.getObjectToRegisterId(),
                            OrganizationStatus.INACTIVE);

                    log.trace("Sending rejected email...");
                    mailProcessor.sendRejectedMailToCustomerByOrganizationId(
                            requestDto.getObjectToRegisterId());

                    log.debug("Organization status is now: {}",
                            OrganizationStatus.INACTIVE);
                }
                default -> {

                    log.warn("Received unexpected request type {}" +
                                    " or status: {}",
                            requestDto.getObjectType(),
                            requestDto.getStatus());
                }
            }
        } catch (Exception e) {

            log.error("Exception occurred while registering organization: {}",
                    e.getMessage());
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
        try {

            OrganizationStatus status =
                    (request.getObjectStatus() == ObjectStatus.ACTIVATE)
                            ? OrganizationStatus.ACTIVE
                            : OrganizationStatus.FROZEN;

            log.trace("Changing status...");
            organizationService.changeStatus(request.getObjectId(), status);

            log.debug("Organization status changed to: {}", status);

        } catch (Exception e) {

            log.error("Failed to change status: {}", e.getMessage());
        }

    }
}

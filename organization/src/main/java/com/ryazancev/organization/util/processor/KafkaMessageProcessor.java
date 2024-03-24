package com.ryazancev.organization.util.processor;

import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.mail.MailType;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.organization.kafka.OrganizationProducerService;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.util.RequestHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageProcessor {

    private final OrganizationProducerService organizationProducerService;
    private final DtoProcessor dtoProcessor;
    private final NotificationProcessor notificationProcessor;

    @Async("asyncTaskExecutor")
    public void sendProductIdToDeleteProductTopic(
            final Long productId) {

        log.info("Method sendProductIdToDeleteProductTopic starts "
                + "work at thread: " + Thread.currentThread().getName());

        organizationProducerService.sendMessageToProductTopic(productId);
    }

    @Async("asyncTaskExecutor")
    public void sendRegistrationRequestToAdmin(
            final Organization organization) {

        log.info("Method sendRegistrationRequestToAdmin starts "
                + "work at thread: " + Thread.currentThread().getName());

        RegistrationRequestDto requestDto = RegistrationRequestDto.builder()
                .objectToRegisterId(organization.getId())
                .objectType(ObjectType.ORGANIZATION)
                .build();

        organizationProducerService.sendMessageToAdminTopic(requestDto);
    }

    @Async("asyncTaskExecutor")
    public void sendNewRegistrationRequestNotification(
            final RequestHeader requestHeader) {

        log.info("Method sendNewRegistrationRequestNotification starts "
                + "work at thread: " + Thread.currentThread().getName());

        NotificationRequest adminNotificationRequest =
                notificationProcessor.createAdminNotification(requestHeader);

        organizationProducerService.sendNotification(adminNotificationRequest);
    }

    public void sendAcceptedMailToCustomerByOrganizationId(
            final Organization organization) {

        log.info("Method sendAcceptedMailToCustomerByOrganizationId starts "
                + "work at thread: " + Thread.currentThread().getName());

        MailDto mailDto = dtoProcessor.createMail(
                organization,
                MailType.OBJECT_REGISTRATION_ACCEPTED);

        organizationProducerService.sendMessageToMailTopic(mailDto);
    }

    public void sendRejectedMailToCustomerByOrganizationId(
            final Organization organization) {

        log.info("Method sendRejectedMailToCustomerByOrganizationId starts "
                + "work at thread: " + Thread.currentThread().getName());

        MailDto mailDto = dtoProcessor.createMail(
                organization,
                MailType.OBJECT_REGISTRATION_REJECTED);

        organizationProducerService.sendMessageToMailTopic(mailDto);
    }
}

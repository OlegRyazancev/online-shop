package com.ryazancev.organization.util.processor;

import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.mail.MailType;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.organization.kafka.OrganizationProducerService;
import com.ryazancev.organization.model.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class KafkaMessageProcessor {

    private final OrganizationProducerService organizationProducerService;
    private final DtoProcessor dtoProcessor;
    private final NotificationProcessor notificationProcessor;

    public void sendProductIdToDeleteProductTopic(Long productId) {

        organizationProducerService.sendMessageToProductTopic(productId);
    }

    public void sendRegistrationRequestToAdmin(Organization organization) {

        RegistrationRequestDto requestDto = RegistrationRequestDto.builder()
                .objectToRegisterId(organization.getId())
                .objectType(ObjectType.ORGANIZATION)
                .build();

        organizationProducerService.sendMessageToAdminTopic(requestDto);
    }

    public void sendAcceptedMailToCustomerByOrganizationId(
            Organization organization) {

        MailDto mailDto = dtoProcessor.createMail(
                organization,
                MailType.OBJECT_REGISTRATION_ACCEPTED);

        organizationProducerService.sendMessageToMailTopic(mailDto);
    }

    public void sendRejectedMailToCustomerByOrganizationId(
            Organization organization) {

        MailDto mailDto = dtoProcessor.createMail(
                organization,
                MailType.OBJECT_REGISTRATION_REJECTED);

        organizationProducerService.sendMessageToMailTopic(mailDto);
    }

    public void sendNewRegistrationRequestNotification() {

        NotificationRequest adminNotificationRequest =
                notificationProcessor.createAdminNotification();

        organizationProducerService.sendNotification(adminNotificationRequest);
    }
}

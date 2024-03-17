package com.ryazancev.organization.util.processor;

import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.mail.MailType;
import com.ryazancev.organization.kafka.OrganizationProducerService;
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

    public void sendProductIdToDeleteProductTopic(Long productId) {

        organizationProducerService.sendMessageToProductTopic(productId);
    }

    public void sendRegistrationRequestToAdmin(Long organizationId) {

        RegistrationRequestDto requestDto = RegistrationRequestDto.builder()
                .objectToRegisterId(organizationId)
                .objectType(ObjectType.ORGANIZATION)
                .build();

        organizationProducerService.sendMessageToAdminTopic(requestDto);
    }

    public void sendAcceptedMailToCustomerByOrganizationId(
            Long organizationId) {

        MailDto mailDto = dtoProcessor.createMail(
                organizationId,
                MailType.OBJECT_REGISTRATION_ACCEPTED);

        organizationProducerService.sendMessageToMailTopic(mailDto);
    }

    public void sendRejectedMailToCustomerByOrganizationId(
            Long organizationId) {

        MailDto mailDto = dtoProcessor.createMail(
                organizationId,
                MailType.OBJECT_REGISTRATION_REJECTED);

        organizationProducerService.sendMessageToMailTopic(mailDto);
    }
}

package com.ryazancev.organization.util;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.mail.MailDto;
import com.ryazancev.dto.mail.MailType;
import com.ryazancev.organization.kafka.OrganizationProducerService;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@RequiredArgsConstructor
public class OrganizationUtil {

    private final OrganizationService organizationService;
    private final OrganizationProducerService organizationProducerService;

    private final CustomerClient customerClient;


    public void
    sendAcceptedMailToCustomerByOrganizationId(Long organizationId) {
        MailDto mailDto = createMail(
                organizationId,
                MailType.ORGANIZATION_REGISTRATION_ACCEPTED);

        organizationProducerService.sendMessageToMailTopic(mailDto);
    }

    public void
    sendRejectedMailToCustomerByOrganizationId(Long organizationId) {
        MailDto mailDto = createMail(
                organizationId,
                MailType.ORGANIZATION_REGISTRATION_REJECTED);

        organizationProducerService.sendMessageToMailTopic(mailDto);
    }


    private MailDto createMail(Long organizationId,
                               MailType mailType) {

        boolean statusCheck = false;
        Organization registered = organizationService.getById(
                organizationId, statusCheck);

        CustomerDto customerDto = customerClient
                .getSimpleById(registered.getOwnerId());
        Properties properties = new Properties();
        properties.setProperty("organization_name", registered.getName());

        return MailDto.builder()
                .email(customerDto.getEmail())
                .type(mailType)
                .name(customerDto.getUsername())
                .properties(properties)
                .build();
    }
}

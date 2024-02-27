package com.ryazancev.organization.util;

import com.ryazancev.dto.admin.enums.ObjectType;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.mail.MailDto;
import com.ryazancev.dto.mail.MailType;
import com.ryazancev.dto.organization.OrganizationDto;
import com.ryazancev.organization.kafka.OrganizationProducerService;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.service.ClientsService;
import com.ryazancev.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@RequiredArgsConstructor
public class OrganizationUtil {

    private final OrganizationService organizationService;
    private final OrganizationProducerService organizationProducerService;

    private final ClientsService clientsService;


    public void setOwnerDto(OrganizationDto organizationDto, Long ownerId) {

        organizationDto.setOwner(clientsService
                .getSimpleCustomerById(ownerId));
    }

    public void
    sendAcceptedMailToCustomerByOrganizationId(Long organizationId) {
        MailDto mailDto = createMail(
                organizationId,
                MailType.OBJECT_REGISTRATION_ACCEPTED);

        organizationProducerService.sendMessageToMailTopic(mailDto);
    }

    public void
    sendRejectedMailToCustomerByOrganizationId(Long organizationId) {
        MailDto mailDto = createMail(
                organizationId,
                MailType.OBJECT_REGISTRATION_REJECTED);

        organizationProducerService.sendMessageToMailTopic(mailDto);
    }


    private MailDto createMail(Long organizationId,
                               MailType mailType) {

        boolean statusCheck = false;
        Organization registered = organizationService.getById(
                organizationId, statusCheck);

        CustomerDto customerDto = clientsService
                .getSimpleCustomerById(registered.getOwnerId())
                .safelyCast(CustomerDto.class);

        Properties properties = new Properties();

        properties.setProperty(
                "object_name", registered.getName());
        properties.setProperty(
                "object_type", ObjectType.ORGANIZATION.name().toLowerCase());

        return MailDto.builder()
                .email(customerDto.getEmail())
                .type(mailType)
                .name(customerDto.getUsername())
                .properties(properties)
                .build();
    }
}

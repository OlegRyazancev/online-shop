package com.ryazancev.organization.util;

import com.ryazancev.dto.Element;
import com.ryazancev.dto.Fallback;
import com.ryazancev.dto.admin.enums.ObjectType;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.mail.MailDto;
import com.ryazancev.dto.mail.MailType;
import com.ryazancev.dto.organization.OrganizationDto;
import com.ryazancev.organization.kafka.OrganizationProducerService;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.service.ClientsService;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.exception.custom.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static com.ryazancev.organization.util.exception.Message.CUSTOMER_SERVICE_UNAVAILABLE;

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

        Element customerObj = clientsService
                .getSimpleCustomerById(registered.getOwnerId());

        if (customerObj instanceof Fallback) {

            throw new ServiceUnavailableException(
                    CUSTOMER_SERVICE_UNAVAILABLE,
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        CustomerDto customerDto = (CustomerDto) customerObj;
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

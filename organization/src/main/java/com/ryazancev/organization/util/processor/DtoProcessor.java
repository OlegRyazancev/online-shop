package com.ryazancev.organization.util.processor;

import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.mail.MailType;
import com.ryazancev.common.dto.organization.OrganizationDto;
import com.ryazancev.common.dto.organization.OrganizationsSimpleResponse;
import com.ryazancev.common.dto.product.ProductsSimpleResponse;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.service.ClientsService;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.mapper.OrganizationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class DtoProcessor {

    private final OrganizationMapper organizationMapper;
    private final ClientsService clientsService;
    private final OrganizationService organizationService;

    public OrganizationsSimpleResponse createOrganizationsSimpleResponse(
            List<Organization> organizations) {

        return OrganizationsSimpleResponse.builder()
                .organizations(
                        organizationMapper.toSimpleListDto(organizations))
                .build();
    }

    public OrganizationDto createOrganizationDetailedDtoWithOwner(
            Organization organization) {

        OrganizationDto organizationDto =
                organizationMapper.toDetailedDto(organization);

        organizationDto.setOwner(clientsService
                .getSimpleCustomerById(organization.getOwnerId()));

        return organizationDto;
    }

    public OrganizationDto createOrganizationSimpleDto(
            Organization organization) {

        return organizationMapper.toSimpleDto(organization);
    }

    public ProductsSimpleResponse createProductsSimpleResponse(Long id) {

        return (ProductsSimpleResponse) clientsService
                .getProductsByOrganizationId(id);
    }

    public MailDto createMail(Long organizationId,
                               MailType mailType) {

        boolean statusCheck = false;
        Organization registered = organizationService.getById(
                organizationId, statusCheck);

        CustomerDto customerDto = clientsService
                .getSimpleCustomerById(registered.getOwnerId())
                .safelyCast(CustomerDto.class, true);

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

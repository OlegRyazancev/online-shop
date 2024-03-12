package com.ryazancev.organization.util;

import com.ryazancev.common.dto.organization.OrganizationDto;
import com.ryazancev.common.dto.organization.OrganizationsSimpleResponse;
import com.ryazancev.common.dto.product.ProductsSimpleResponse;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.service.ClientsService;
import com.ryazancev.organization.util.mapper.OrganizationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class DtoProcessor {

    private final OrganizationMapper organizationMapper;
    private final ClientsService clientsService;

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
}

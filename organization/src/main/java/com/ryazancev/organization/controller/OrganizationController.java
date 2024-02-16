package com.ryazancev.organization.controller;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.logo.LogoDto;
import com.ryazancev.dto.organization.OrganizationDto;
import com.ryazancev.dto.organization.OrganizationEditDto;
import com.ryazancev.dto.organization.OrganizationsSimpleResponse;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.service.expression.CustomExpressionService;
import com.ryazancev.organization.util.mapper.OrganizationMapper;
import com.ryazancev.validation.OnCreate;
import com.ryazancev.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/organizations")
@RequiredArgsConstructor
@Validated
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;

    private final CustomExpressionService customExpressionService;

    private final CustomerClient customerClient;
    private final ProductClient productClient;

    @GetMapping
    public OrganizationsSimpleResponse getAll() {

        customExpressionService.checkIfAccountLocked();

        List<Organization> organizations = organizationService.getAll();

        return OrganizationsSimpleResponse.builder()
                .organizations(
                        organizationMapper.toSimpleListDto(organizations))
                .build();
    }

    @GetMapping("/{id}")
    public OrganizationDto getById(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();

        boolean statusCheck = true;

        Organization organization =
                organizationService.getById(id, statusCheck);
        OrganizationDto organizationDto =
                organizationMapper.toDetailedDto(organization);

        CustomerDto owner =
                customerClient.getSimpleById(organization.getOwnerId());
        organizationDto.setOwner(owner);

        return organizationDto;
    }

    @PostMapping
    public OrganizationDto makeRegistrationRequest(
            @RequestBody
            @Validated(OnCreate.class)
            OrganizationEditDto organizationEditDto) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessUser(organizationEditDto);

        Organization organization =
                organizationMapper.toEntity(organizationEditDto);
        Organization saved =
                organizationService.makeRegistrationRequest(organization);
        OrganizationDto organizationDto =
                organizationMapper.toDetailedDto(saved);

        CustomerDto owner = customerClient.getSimpleById(
                organization.getOwnerId());
        organizationDto.setOwner(owner);

        return organizationDto;
    }

    @PutMapping
    public OrganizationDto update(
            @RequestBody
            @Validated(OnUpdate.class)
            OrganizationEditDto organizationEditDto) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService
                .checkAccessOrganization(organizationEditDto.getId());

        Organization organization =
                organizationMapper.toEntity(organizationEditDto);
        Organization updated = organizationService.update(organization);
        OrganizationDto organizationDto =
                organizationMapper.toDetailedDto(updated);

        CustomerDto owner =
                customerClient.getSimpleById(updated.getOwnerId());
        organizationDto.setOwner(owner);


        return organizationDto;
    }

    @GetMapping("/{id}/products")
    public ProductsSimpleResponse getProductsByOrganizationId(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();

        return productClient.getProductsByOrganizationId(id);
    }

    @PostMapping("/{id}/logo")
    public void uploadLogo(
            @PathVariable("id") Long id,
            @Validated(OnCreate.class)
            @ModelAttribute LogoDto logoDto) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessOrganization(id);

        organizationService.uploadLogo(id, logoDto);
    }

    @DeleteMapping("/{id}")
    public String deleteOrganizationById(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessOrganization(id);

        return organizationService.markOrganizationAsDeleted(id);
    }


//    Endpoints only  for feign clients

    @GetMapping("/{id}/simple")
    public OrganizationDto getSimpleById(
            @PathVariable("id") Long id) {

        boolean statusCheck = false;

        Organization organization =
                organizationService.getById(id, statusCheck);

        return organizationMapper.toSimpleDto(organization);
    }

    @GetMapping("/{id}/owner-id")
    public Long getOwnerId(
            @PathVariable("id") Long id) {

        return organizationService.getOwnerId(id);
    }
}

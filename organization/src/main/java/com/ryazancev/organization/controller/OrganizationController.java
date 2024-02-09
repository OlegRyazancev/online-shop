package com.ryazancev.organization.controller;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.dto.customer.CustomerDTO;
import com.ryazancev.dto.logo.LogoDTO;
import com.ryazancev.dto.organization.OrganizationDTO;
import com.ryazancev.dto.organization.OrganizationEditDTO;
import com.ryazancev.dto.organization.OrganizationsSimpleResponse;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.service.expression.CustomExpressionService;
import com.ryazancev.organization.util.exception.custom.AccessDeniedException;
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

        List<Organization> organizations = organizationService.getAll();

        return OrganizationsSimpleResponse.builder()
                .organizations(
                        organizationMapper.toSimpleListDTO(organizations))
                .build();
    }

    @GetMapping("/{id}")
    public OrganizationDTO getById(
            @PathVariable("id") Long id) {

        Organization organization = organizationService.getById(id);
        OrganizationDTO organizationDTO =
                organizationMapper.toDetailedDTO(organization);

        CustomerDTO owner =
                customerClient.getSimpleById(organization.getOwnerId());
        organizationDTO.setOwner(owner);

        return organizationDTO;
    }

    @PostMapping
    public OrganizationDTO makeRegistrationRequest(
            @RequestBody
            @Validated(OnCreate.class)
            OrganizationEditDTO organizationEditDTO) {

        if (!customExpressionService
                .canAccessUser(organizationEditDTO.getOwnerId())) {

            throw new AccessDeniedException();
        }

        Organization organization =
                organizationMapper.toEntity(organizationEditDTO);
        Organization saved =
                organizationService.makeRegistrationRequest(organization);
        OrganizationDTO organizationDTO =
                organizationMapper.toDetailedDTO(saved);

        CustomerDTO owner = customerClient.getSimpleById(
                organization.getOwnerId());
        organizationDTO.setOwner(owner);

        return organizationDTO;
    }

    @PutMapping
    public OrganizationDTO update(
            @RequestBody
            @Validated(OnUpdate.class)
            OrganizationEditDTO organizationEditDTO) {

        if (!customExpressionService
                .canAccessOrganization(organizationEditDTO.getId())) {

            throw new AccessDeniedException();
        }

        Organization organization =
                organizationMapper.toEntity(organizationEditDTO);
        Organization updated = organizationService.update(organization);
        OrganizationDTO organizationDTO =
                organizationMapper.toDetailedDTO(updated);

        CustomerDTO owner =
                customerClient.getSimpleById(updated.getOwnerId());
        organizationDTO.setOwner(owner);


        return organizationDTO;
    }

    @GetMapping("/{id}/products")
    public ProductsSimpleResponse getProductsByOrganizationId(
            @PathVariable("id") Long id) {

        return productClient.getProductsByOrganizationId(id);
    }

    @PostMapping("/{id}/logo")
    public void uploadLogo(
            @PathVariable("id") Long id,
            @Validated(OnCreate.class)
            @ModelAttribute LogoDTO logoDto) {

        if (!customExpressionService.canAccessOrganization(id)) {
            throw new AccessDeniedException();
        }

        organizationService.uploadLogo(id, logoDto);
    }


//    Endpoints only  for feign clients

    @GetMapping("/{id}/simple")
    public OrganizationDTO getSimpleById(
            @PathVariable("id") Long id) {

        Organization organization = organizationService.getById(id);

        return organizationMapper.toSimpleDTO(organization);
    }

    @GetMapping("/{id}/owner")
    public Long getOwnerId(
            @PathVariable("id") Long id) {

        return organizationService.getOwnerId(id);
    }
}

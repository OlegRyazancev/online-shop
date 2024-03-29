package com.ryazancev.organization.controller;

import com.ryazancev.common.dto.logo.LogoDto;
import com.ryazancev.common.dto.organization.OrganizationDto;
import com.ryazancev.common.dto.organization.OrganizationEditDto;
import com.ryazancev.common.dto.organization.OrganizationsSimpleResponse;
import com.ryazancev.common.dto.product.ProductsSimpleResponse;
import com.ryazancev.common.validation.OnCreate;
import com.ryazancev.common.validation.OnUpdate;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.service.CustomExpressionService;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.mapper.OrganizationMapper;
import com.ryazancev.organization.util.processor.DtoProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@RestController
@RequestMapping("api/v1/organizations")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Organization controller",
        description = "Organization related methods"
)
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;

    private final DtoProcessor dtoProcessor;

    private final CustomExpressionService customExpressionService;


    @GetMapping
    @Operation(
            summary = "Get all organizations",
            description = "List(page) of all existing "
                    + "(SIMPLE) organizations in database"
    )
    public OrganizationsSimpleResponse getAll() {

        customExpressionService.checkIfAccountLocked();

        List<Organization> organizations = organizationService.getAll();

        return dtoProcessor.createOrganizationsSimpleResponse(organizations);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get organization by id",
            description = "Retrieve detailed information about "
                    + "an organization from the database based on its unique "
                    + "identifier (ID), including owner details"
    )
    public OrganizationDto getById(
            @Parameter(
                    description = "Organization ID",
                    example = "1"
            )
            @PathVariable("id") final Long id) {

        customExpressionService.checkIfAccountLocked();

        boolean statusCheck = true;

        Organization organization =
                organizationService.getById(id, statusCheck);

        return dtoProcessor
                .createOrganizationDetailedDtoWithOwner(organization);
    }

    @PostMapping
    @Operation(
            summary = "Make registration request for a new organization",
            description = " Registers a new organization based on the "
                    + "provided details"
    )
    public OrganizationDto makeRegistrationRequest(
            @Parameter(
                    description = "Organization details for make "
                            + "registration request",
                    required = true
            )
            @Validated(OnCreate.class)
            @RequestBody final OrganizationEditDto organizationEditDto) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessUser(
                organizationEditDto.getOwnerId());

        Organization organization =
                organizationMapper.toEntity(organizationEditDto);
        Organization saved =
                organizationService.makeRegistrationRequest(organization);

        return dtoProcessor.createOrganizationDetailedDtoWithOwner(saved);
    }

    @PutMapping
    @Operation(
            summary = "Update organization details",
            description = "Updates the details of an existing "
                    + "organization based on the provided information"
    )
    public OrganizationDto update(
            @Parameter(
                    description = "Updated organization details",
                    required = true
            )
            @Validated(OnUpdate.class)
            @RequestBody final OrganizationEditDto organizationEditDto) {

        customExpressionService.checkAccountConditions();
        customExpressionService
                .checkAccessOrganization(organizationEditDto.getId());

        Organization organization =
                organizationMapper.toEntity(organizationEditDto);
        Organization updated = organizationService.update(organization);

        return dtoProcessor.createOrganizationDetailedDtoWithOwner(updated);
    }

    @GetMapping("/{id}/products")
    @Operation(
            summary = "Get products by organization ID",
            description = "Retrieves products associated "
                    + "with the specified organization ID"
    )
    public ProductsSimpleResponse getProductsByOrganizationId(
            @Parameter(
                    description = "Organization ID",
                    example = "1"
            )
            @PathVariable("id") final Long id) {

        customExpressionService.checkIfAccountLocked();

        boolean statusCheck = false;

        Organization organization = organizationService
                .getById(id, statusCheck);

        return dtoProcessor.createProductsSimpleResponse(organization.getId());
    }

    @PostMapping("/{id}/logo")
    @Operation(
            summary = "Upload organization logo",
            description = "Uploads a logo for the organization "
                    + "with the specified ID"
    )
    public String uploadLogo(
            @Parameter(
                    description = "Logo information",
                    required = true
            )
            @PathVariable("id") final Long id,
            @Validated(OnCreate.class)
            @ModelAttribute final LogoDto logoDto) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessOrganization(id);

        return organizationService.uploadLogo(id, logoDto);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete organization by ID",
            description = "Marks the organization with the "
                    + "specified ID as deleted"
    )
    public String deleteOrganizationById(
            @Parameter(
                    description = "Organization ID",
                    example = "1"
            )
            @PathVariable("id") final Long id) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessOrganization(id);

        return organizationService.markOrganizationAsDeleted(id);
    }


//    Endpoints only  for feign clients

    @GetMapping("/{id}/simple")
    @Operation(
            summary = "Get simple organization by ID. FOR FEIGN CLIENTS",
            description = "Retrieves basic information (id, name) "
                    + "about the organization with the specified ID."
    )
    public OrganizationDto getSimpleById(
            @Parameter(
                    description = "Organization ID",
                    example = "1"
            )
            @PathVariable("id") final Long id) {

        boolean statusCheck = false;

        Organization organization =
                organizationService.getById(id, statusCheck);

        return dtoProcessor.createOrganizationSimpleDto(organization);
    }

    @GetMapping("/{id}/owner-id")
    @Operation(
            summary = "Get owner ID of organization. FOR FEIGN CLIENTS",
            description = "Retrieves the owner ID of the organization "
                    + "with the specified ID."
    )
    public Long getOwnerId(
            @Parameter(
                    description = "Organization ID",
                    example = "1"
            )
            @PathVariable("id") final Long id) {

        return organizationService.getOwnerId(id);
    }
}

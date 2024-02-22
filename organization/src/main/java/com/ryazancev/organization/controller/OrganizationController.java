package com.ryazancev.organization.controller;

import com.ryazancev.clients.ProductClient;
import com.ryazancev.dto.logo.LogoDto;
import com.ryazancev.dto.organization.OrganizationDto;
import com.ryazancev.dto.organization.OrganizationEditDto;
import com.ryazancev.dto.organization.OrganizationsSimpleResponse;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.service.expression.CustomExpressionService;
import com.ryazancev.organization.util.OrganizationUtil;
import com.ryazancev.organization.util.mapper.OrganizationMapper;
import com.ryazancev.validation.OnCreate;
import com.ryazancev.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Organization controller",
        description = "Organization related methods"
)
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;
    private final OrganizationUtil organizationUtil;

    private final CustomExpressionService customExpressionService;

    private final ProductClient productClient;

    @GetMapping
    @Operation(
            summary = "Get all organizations",
            description = "List(page) of all existing " +
                    "(SIMPLE) organizations in database"
    )
    public OrganizationsSimpleResponse getAll() {

        customExpressionService.checkIfAccountLocked();

        List<Organization> organizations = organizationService.getAll();

        return OrganizationsSimpleResponse.builder()
                .organizations(
                        organizationMapper.toSimpleListDto(organizations))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get organization by id",
            description = "Retrieve detailed information about " +
                    "an organization from the database based on its unique " +
                    "identifier (ID), including owner details"
    )
    public OrganizationDto getById(
            @PathVariable("id")
            @Parameter(
                    description = "Organization ID",
                    example = "1"
            )
            Long id) {

        customExpressionService.checkIfAccountLocked();

        boolean statusCheck = true;

        Organization organization =
                organizationService.getById(id, statusCheck);
        OrganizationDto organizationDto =
                organizationMapper.toDetailedDto(organization);

        organizationUtil.enrichOrganizationDto(organizationDto,
                organization.getOwnerId());

        return organizationDto;
    }

    @PostMapping
    @Operation(
            summary = "Make registration request for a new organization",
            description = " Registers a new organization based on the " +
                    "provided details"
    )
    public OrganizationDto makeRegistrationRequest(
            @RequestBody
            @Validated(OnCreate.class)
            @Parameter(
                    description = "Organization details for make " +
                            "registration request",
                    required = true
            )
            OrganizationEditDto organizationEditDto) {

        customExpressionService.checkIfEmailConfirmed();
        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessUser(organizationEditDto);

        Organization organization =
                organizationMapper.toEntity(organizationEditDto);
        Organization saved =
                organizationService.makeRegistrationRequest(organization);
        OrganizationDto organizationDto =
                organizationMapper.toDetailedDto(saved);

        organizationUtil.enrichOrganizationDto(organizationDto,
                organization.getOwnerId());

        return organizationDto;
    }

    @PutMapping
    @Operation(
            summary = "Update organization details",
            description = "Updates the details of an existing " +
                    "organization based on the provided information"
    )
    public OrganizationDto update(
            @RequestBody
            @Validated(OnUpdate.class)
            @Parameter(
                    description = "Updated organization details",
                    required = true
            )
            OrganizationEditDto organizationEditDto) {

        customExpressionService.checkIfEmailConfirmed();
        customExpressionService.checkIfAccountLocked();
        customExpressionService
                .checkAccessOrganization(organizationEditDto.getId());

        Organization organization =
                organizationMapper.toEntity(organizationEditDto);
        Organization updated = organizationService.update(organization);
        OrganizationDto organizationDto =
                organizationMapper.toDetailedDto(updated);

        organizationUtil.enrichOrganizationDto(organizationDto,
                organization.getOwnerId());

        return organizationDto;
    }

    @GetMapping("/{id}/products")
    @Operation(
            summary = "Get products by organization ID",
            description = "Retrieves products associated " +
                    "with the specified organization ID"
    )
    public ProductsSimpleResponse getProductsByOrganizationId(
            @PathVariable("id")
            @Parameter(
                    description = "Organization ID",
                    example = "1"
            )
            Long id) {

        customExpressionService.checkIfAccountLocked();

        return productClient.getProductsByOrganizationId(id);
    }

    @PostMapping("/{id}/logo")
    @Operation(
            summary = "Upload organization logo",
            description = "Uploads a logo for the organization " +
                    "with the specified ID"
    )
    public void uploadLogo(
            @PathVariable("id") Long id,
            @Validated(OnCreate.class)
            @ModelAttribute
            @Parameter(
                    description = "Logo information",
                    required = true
            )
            LogoDto logoDto) {

        customExpressionService.checkIfEmailConfirmed();
        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessOrganization(id);

        organizationService.uploadLogo(id, logoDto);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete organization by ID",
            description = "Marks the organization with the " +
                    "specified ID as deleted"
    )
    public String deleteOrganizationById(
            @PathVariable("id")
            @Parameter(
                    description = "Organization ID",
                    example = "1"
            )
            Long id) {

        customExpressionService.checkIfEmailConfirmed();
        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessOrganization(id);

        return organizationService.markOrganizationAsDeleted(id);
    }


//    Endpoints only  for feign clients

    @GetMapping("/{id}/simple")
    @Operation(
            summary = "Get simple organization by ID. FOR FEIGN CLIENTS",
            description = "Retrieves basic information (id, name) " +
                    "about the organization with the specified ID."
    )
    public OrganizationDto getSimpleById(
            @PathVariable("id")
            @Parameter(
                    description = "Organization ID",
                    example = "1"
            )
            Long id) {

        boolean statusCheck = false;

        Organization organization =
                organizationService.getById(id, statusCheck);

        return organizationMapper.toSimpleDto(organization);
    }

    @GetMapping("/{id}/owner-id")
    @Operation(
            summary = "Get owner ID of organization. FOR FEIGN CLIENTS",
            description = "Retrieves the owner ID of the organization " +
                    "with the specified ID."
    )
    public Long getOwnerId(
            @PathVariable("id")
            @Parameter(
                    description = "Organization ID",
                    example = "1"
            )
            Long id) {

        return organizationService.getOwnerId(id);
    }
}

package com.ryazancev.organization.controller;

import com.ryazancev.dto.logo.LogoDTO;
import com.ryazancev.dto.organization.OrganizationDTO;
import com.ryazancev.dto.organization.OrganizationEditDTO;
import com.ryazancev.dto.organization.OrganizationsSimpleResponse;
import com.ryazancev.organization.expression.CustomExpressionService;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.exception.custom.AccessDeniedException;
import com.ryazancev.validation.OnCreate;
import com.ryazancev.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/organizations")
@RequiredArgsConstructor
@Validated
public class OrganizationController {

    private final OrganizationService organizationService;
    private final CustomExpressionService customExpressionService;

    @GetMapping
    public OrganizationsSimpleResponse getAll() {

        return organizationService.getAll();
    }

    @GetMapping("/{id}")
    public OrganizationDTO getById(
            @PathVariable("id") Long id) {

        return organizationService.getDetailedById(id);
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

        return organizationService
                .makeRegistrationRequest(organizationEditDTO);
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

        return organizationService.update(organizationEditDTO);
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

        return organizationService.getSimpleById(id);
    }

    @GetMapping("/{id}/owner")
    public Long getOwnerId(@PathVariable("id") Long id) {

        return organizationService.getOwnerId(id);
    }
}

package com.ryazancev.organization.controller;

import com.ryazancev.clients.logo.dto.LogoDTO;
import com.ryazancev.clients.organization.dto.OrganizationDTO;
import com.ryazancev.clients.organization.dto.OrganizationEditDTO;
import com.ryazancev.clients.organization.dto.OrganizationsSimpleListResponse;
import com.ryazancev.organization.service.OrganizationService;
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

    @GetMapping
    public OrganizationsSimpleListResponse getAll() {

        return organizationService.getAll();
    }

    @GetMapping("/{id}")
    public OrganizationDTO getSimpleById(
            @PathVariable("id") Long id) {

        return organizationService.getSimpleById(id);
    }

    @GetMapping("/{id}/details")
    public OrganizationDTO getDetailedById(
            @PathVariable("id") Long id) {

        return organizationService.getDetailedById(id);
    }

    @PostMapping
    public OrganizationDTO register(
            @RequestBody
            @Validated(OnCreate.class)
            OrganizationEditDTO organizationEditDTO) {

        return organizationService.register(organizationEditDTO);
    }

    @PutMapping
    public OrganizationDTO update(
            @RequestBody
            @Validated(OnUpdate.class)
            OrganizationEditDTO organizationEditDTO) {

        return organizationService.update(organizationEditDTO);
    }

    @PostMapping("/{id}/logo")
    public void uploadLogo(
            @PathVariable("id") Long id,
            @Validated(OnCreate.class)
            @ModelAttribute LogoDTO logoDto) {

        organizationService.uploadLogo(id, logoDto);
    }
}

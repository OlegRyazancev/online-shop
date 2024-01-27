package com.ryazancev.organization.controller;

import com.ryazancev.clients.logo.LogoDTO;
import com.ryazancev.clients.organization.*;
import com.ryazancev.organization.service.OrganizationService;
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
    public OrganizationsListResponse getAll() {

        return organizationService.getAll();
    }

    @GetMapping("/{id}")
    public OrganizationSimpleDTO getSimpleById(
            @PathVariable("id") Long id) {

        return organizationService.getSimpleById(id);
    }

    @GetMapping("/{id}/details")
    public OrganizationDetailedDTO getDetailedById(
            @PathVariable("id") Long id) {

        return organizationService.getDetailedById(id);
    }

    @PostMapping
    public OrganizationDetailedDTO register(
            @RequestBody OrganizationCreateDTO organizationCreateDTO) {

        return organizationService.register(organizationCreateDTO);
    }

    @PutMapping
    public OrganizationDetailedDTO update(
            @RequestBody OrganizationUpdateDTO organizationUpdateDTO) {

        return organizationService.update(organizationUpdateDTO);
    }

    @PostMapping("/{id}/logo")
    public void uploadLogo(
            @PathVariable("id") Long id,
            @Validated @ModelAttribute LogoDTO logoDto) {

        organizationService.uploadLogo(id, logoDto);
    }
}

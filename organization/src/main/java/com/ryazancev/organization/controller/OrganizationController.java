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

    @GetMapping("/{organizationId}")
    public OrganizationDTO getById(@PathVariable("organizationId") Long organizationId) {
        return organizationService.getById(organizationId);
    }

    @GetMapping("/{organizationId}/details")
    public OrganizationDetailedDTO getDetailedById(@PathVariable("organizationId") Long organizationId) {
        return organizationService.getDetailedById(organizationId);
    }

    @PostMapping
    public OrganizationDetailedDTO register(@RequestBody OrganizationCreateDTO organizationCreateDTO) {
        return organizationService.register(organizationCreateDTO);
    }

    @PutMapping
    public OrganizationDetailedDTO update(@RequestBody OrganizationUpdateDTO organizationUpdateDTO) {
        return organizationService.update(organizationUpdateDTO);
    }

    @PostMapping("/{organizationId}/logo")
    public void uploadLogo(@PathVariable("organizationId") Long organizationId,
                            @Validated @ModelAttribute LogoDTO logoDto) {
        organizationService.uploadLogo(organizationId, logoDto);
        log.info("Logo uploaded successfully in organization microservice");
    }
}

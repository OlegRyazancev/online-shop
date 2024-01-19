package com.ryazancev.organization.controller;

import com.ryazancev.clients.organization.OrganizationDetailedDTO;
import com.ryazancev.clients.organization.OrganizationsListResponse;
import com.ryazancev.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/organizations")
@RequiredArgsConstructor
@Validated
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<OrganizationsListResponse> getOrganizations() {
        OrganizationsListResponse organizations = organizationService.getAll();
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationDetailedDTO> getById(@PathVariable("organizationId") Long organizationId) {
        OrganizationDetailedDTO organization = organizationService.getById(organizationId);
        return ResponseEntity.ok(organization);
    }
}

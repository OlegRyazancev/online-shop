package com.ryazancev.organization.controller;

import com.ryazancev.clients.organization.OrganizationDTO;
import com.ryazancev.clients.organization.OrganizationDetailedDTO;
import com.ryazancev.clients.organization.OrganizationsListResponse;
import com.ryazancev.clients.product.ProductDetailedDTO;
import com.ryazancev.clients.product.ProductUpdateDTO;
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
    public OrganizationsListResponse getOrganizations() {
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

    //todo: register organization

    //todo: update organization

    //todo: get products by organizationId

    @PutMapping("/{organizationId}/products")
    public ProductDetailedDTO updateProduct(
            @PathVariable("organizationId") Long organizationId,
            @RequestBody ProductUpdateDTO productUpdateDTO) {

        log.info("Product update id: {}", productUpdateDTO.getId());
        return organizationService.update(organizationId, productUpdateDTO);
    }
    //todo: update product

    //todo:create product

    //todo: delete product
}

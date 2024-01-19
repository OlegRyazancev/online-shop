package com.ryazancev.organization.service.impl;

import com.ryazancev.clients.organization.OrganizationDetailedDTO;
import com.ryazancev.clients.organization.OrganizationsListResponse;
import com.ryazancev.clients.product.ProductClient;
import com.ryazancev.clients.product.ProductListResponse;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.mappers.OrganizationMapper;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private final ProductClient productClient;

    @Override
    public OrganizationsListResponse getAll() {
        List<Organization> organizations = organizationRepository.findAll();
        return OrganizationsListResponse.builder()
                .organizations(organizationMapper.toDTO(organizations))
                .build();
    }

    @Override
    public OrganizationDetailedDTO getById(Long organizationId) {
        //todo:add exception checks

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() ->
                        new NotFoundException("Organization not found"));

        ProductListResponse response = productClient.getByOrganizationId(organizationId);
        OrganizationDetailedDTO organizationDTO = organizationMapper.toDTO(organization);
        System.out.println(response.getProducts().size());
        organizationDTO.setProducts(response.getProducts());
        organizationDTO.setLogo("Logo1");

        return organizationDTO;
    }
}

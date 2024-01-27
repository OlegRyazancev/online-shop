package com.ryazancev.organization.service.impl;

import com.ryazancev.clients.customer.CustomerClient;
import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.logo.LogoClient;
import com.ryazancev.clients.logo.LogoDTO;
import com.ryazancev.clients.organization.*;
import com.ryazancev.clients.product.ProductClient;
import com.ryazancev.clients.product.ProductListResponse;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.exception.custom.OrganizationCreationException;
import com.ryazancev.organization.util.exception.custom.OrganizationNotFoundException;
import com.ryazancev.organization.util.mapper.OrganizationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final LogoClient logoClient;
    private final CustomerClient customerClient;

    @Override
    public OrganizationsListResponse getAll() {

        List<Organization> organizations = organizationRepository.findAll();

        return OrganizationsListResponse.builder()
                .organizations(organizationMapper
                        .toSimpleListDTO(organizations))
                .build();
    }

    @Override
    public OrganizationSimpleDTO getSimpleById(Long id) {

        Organization existing = findById(id);

        return organizationMapper.toSimpleDTO(existing);
    }

    @Override
    public OrganizationDetailedDTO getDetailedById(Long id) {

        Organization existing = findById(id);

        ProductListResponse orgProducts = productClient
                .getProductsByOrganizationId(existing.getId());

        OrganizationDetailedDTO organizationDTO = organizationMapper
                .toDetailedDTO(existing);
        organizationDTO.setProducts(orgProducts.getProducts());

        if (existing.getOwnerId() != null) {
            CustomerDTO owner = customerClient
                    .getSimpleById(existing.getOwnerId());
            organizationDTO.setOwner(owner);
        }

        return organizationDTO;
    }

    @Transactional
    @Override
    public OrganizationDetailedDTO register(
            OrganizationCreateDTO organizationCreateDTO) {

        if (organizationRepository
                .findByName(organizationCreateDTO.getName())
                .isPresent()) {
            throw new OrganizationCreationException(
                    "Organization with this name already exists",
                    HttpStatus.BAD_REQUEST
            );
        }
        Organization organizationToSave = organizationMapper
                .toEntity(organizationCreateDTO);
        Organization savedOrganization = organizationRepository
                .save(organizationToSave);

        OrganizationDetailedDTO registered = organizationMapper
                .toDetailedDTO(savedOrganization);

        CustomerDTO owner = customerClient
                .getSimpleById(savedOrganization.getOwnerId());

        registered.setOwner(owner);

        return registered;
    }

    @Transactional
    @Override
    public OrganizationDetailedDTO update(
            OrganizationUpdateDTO organizationUpdateDTO) {

        Organization existing = findById(organizationUpdateDTO.getId());
        existing.setName(organizationUpdateDTO.getName());
        existing.setDescription(organizationUpdateDTO.getDescription());
        existing.setOwnerId(organizationUpdateDTO.getOwnerId());

        Organization updated = organizationRepository.save(existing);
        OrganizationDetailedDTO updatedDTO = organizationMapper
                .toDetailedDTO(updated);

        CustomerDTO owner = customerClient
                .getSimpleById(updated.getOwnerId());
        updatedDTO.setOwner(owner);

        ProductListResponse productsResponse = productClient
                .getProductsByOrganizationId(updated.getId());
        updatedDTO.setProducts(productsResponse.getProducts());

        return updatedDTO;
    }

    @Transactional
    @Override
    public void uploadLogo(Long id, LogoDTO logoDTO) {

        Organization existing = findById(id);

        String fileName = logoClient.upload(logoDTO.getFile());

        existing.setLogo(fileName);
        organizationRepository.save(existing);
    }

    private Organization findById(Long id) {

        return organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(
                        "Organization not found",
                        HttpStatus.NOT_FOUND
                ));
    }
}





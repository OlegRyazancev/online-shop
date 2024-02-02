package com.ryazancev.organization.service.impl;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.LogoClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.dto.customer.CustomerDTO;
import com.ryazancev.dto.logo.LogoDTO;
import com.ryazancev.dto.organization.OrganizationDTO;
import com.ryazancev.dto.organization.OrganizationEditDTO;
import com.ryazancev.dto.organization.OrganizationsSimpleResponse;
import com.ryazancev.dto.product.ProductsSimpleResponse;
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
    public OrganizationsSimpleResponse getAll() {

        List<Organization> organizations = organizationRepository.findAll();

        return OrganizationsSimpleResponse.builder()
                .organizations(organizationMapper
                        .toSimpleListDTO(organizations))
                .build();
    }

    @Override
    public OrganizationDTO getSimpleById(Long id) {

        Organization existing = findById(id);

        return organizationMapper.toSimpleDTO(existing);
    }

    @Override
    public OrganizationDTO getDetailedById(Long id) {

        Organization existing = findById(id);

        ProductsSimpleResponse orgProducts = productClient
                .getProductsByOrganizationId(existing.getId());

        OrganizationDTO organizationDTO = organizationMapper
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
    public OrganizationDTO register(
            OrganizationEditDTO organizationEditDTO) {

        if (organizationRepository
                .findByName(organizationEditDTO.getName())
                .isPresent()) {
            throw new OrganizationCreationException(
                    "Organization with this name already exists",
                    HttpStatus.BAD_REQUEST
            );
        }
        Organization toSave = organizationMapper
                .toEntity(organizationEditDTO);
        Organization saved = organizationRepository
                .save(toSave);

        OrganizationDTO savedDTO = organizationMapper
                .toDetailedDTO(saved);

        CustomerDTO owner = customerClient
                .getSimpleById(saved.getOwnerId());

        savedDTO.setOwner(owner);

        return savedDTO;
    }

    @Transactional
    @Override
    public OrganizationDTO update(
            OrganizationEditDTO organizationEditDTO) {

        Organization existing = findById(organizationEditDTO.getId());
        existing.setName(organizationEditDTO.getName());
        existing.setDescription(organizationEditDTO.getDescription());
        existing.setOwnerId(organizationEditDTO.getOwnerId());

        Organization updated = organizationRepository.save(existing);
        OrganizationDTO updatedDTO = organizationMapper
                .toDetailedDTO(updated);

        CustomerDTO owner = customerClient
                .getSimpleById(updated.getOwnerId());
        updatedDTO.setOwner(owner);

        ProductsSimpleResponse productsResponse = productClient
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





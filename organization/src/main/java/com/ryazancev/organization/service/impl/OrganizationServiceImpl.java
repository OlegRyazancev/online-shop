package com.ryazancev.organization.service.impl;

import com.ryazancev.clients.organization.*;
import com.ryazancev.clients.product.ProductClient;
import com.ryazancev.clients.product.ProductDetailedDTO;
import com.ryazancev.clients.product.ProductListResponse;
import com.ryazancev.clients.product.ProductUpdateDTO;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.exception.OrganizationCreationException;
import com.ryazancev.organization.util.exception.OrganizationNotFoundException;
import com.ryazancev.organization.util.mappers.OrganizationMapper;
import jakarta.ws.rs.NotFoundException;
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

    @Override
    public OrganizationsListResponse getAll() {
        List<Organization> organizations = organizationRepository.findAll();
        return OrganizationsListResponse.builder()
                .organizations(organizationMapper.toDetailedListDTO(organizations))
                .build();
    }

    @Override
    public OrganizationDTO getById(Long organizationId) {
        Organization existing = findById(organizationId);


        return organizationMapper.toSimpleDTO(existing);
    }


    @Override
    public OrganizationDetailedDTO getDetailedById(Long organizationId) {
        //todo:add exception checks

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() ->
                        new NotFoundException("Organization not found"));

        ProductListResponse response = productClient.getByOrganizationId(organizationId);
        OrganizationDetailedDTO organizationDTO = organizationMapper.toDetailedDTO(organization);
        System.out.println(response.getProducts().size());
        organizationDTO.setProducts(response.getProducts());

        //todo: logo client??

        organizationDTO.setLogo("Logo1");

        return organizationDTO;
    }

    @Transactional
    @Override
    public OrganizationDetailedDTO register(OrganizationCreateDTO organizationCreateDTO) {
        if (organizationRepository.findByName(organizationCreateDTO.getName()).isPresent()) {
            throw new OrganizationCreationException(
                    "Organization with this name already exists",
                    HttpStatus.BAD_REQUEST
            );
        }

        Organization organizationToSave = organizationMapper.toEntity(organizationCreateDTO);
        Organization savedOrganization = organizationRepository.save(organizationToSave);
        return organizationMapper.toDetailedDTO(savedOrganization);

    }

    @Transactional
    @Override
    public OrganizationDetailedDTO update(OrganizationUpdateDTO organizationUpdateDTO) {
        if (organizationRepository.findByName(organizationUpdateDTO.getName()).isPresent()) {
            throw new OrganizationCreationException(
                    "Organization with this name already exists",
                    HttpStatus.BAD_REQUEST
            );
        }

        Organization existing = findById(organizationUpdateDTO.getId());
        existing.setName(organizationUpdateDTO.getName());
        existing.setDescription(organizationUpdateDTO.getDescription());
        existing.setLogo(organizationUpdateDTO.getLogo());

        Organization updated = organizationRepository.save(existing);
        return organizationMapper.toDetailedDTO(updated);
    }

    private Organization findById(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(
                                "Organization not found",
                                HttpStatus.NOT_FOUND
                        ));
    }

}

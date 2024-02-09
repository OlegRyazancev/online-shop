package com.ryazancev.organization.service.impl;

import com.ryazancev.clients.LogoClient;
import com.ryazancev.dto.admin.ObjectType;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import com.ryazancev.dto.logo.LogoDTO;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.exception.custom.OrganizationCreationException;
import com.ryazancev.organization.util.exception.custom.OrganizationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final LogoClient logoClient;

    @Value("${spring.kafka.topic.admin}")
    private String adminTopic;

    private final KafkaTemplate<String, RegistrationRequestDTO> kafkaTemplate;

    @Override
    public List<Organization> getAll() {

        return organizationRepository.findAll();
    }

    @Override
    public Organization getById(Long id) {

        return findById(id);
    }

    @Transactional
    @Override
    public Organization makeRegistrationRequest(
            Organization organization) {

        if (organizationRepository
                .findByName(organization.getName())
                .isPresent()) {

            throw new OrganizationCreationException(
                    "Organization with this name already exists",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (organizationRepository
                .findByDescription(organization.getDescription())
                .isPresent()) {

            throw new OrganizationCreationException(
                    "Organization with this description already exists",
                    HttpStatus.BAD_REQUEST
            );
        }

        organization.setStatus(OrganizationStatus.INACTIVE);

        Organization saved = organizationRepository.save(organization);

        sendRegistrationRequestToAdmin(saved.getId());

        return saved;
    }

    @Transactional
    @Override
    public Organization update(Organization organization) {

        Organization existing = findById(organization.getId());

        existing.setName(organization.getName());
        existing.setDescription(organization.getDescription());
        existing.setOwnerId(organization.getOwnerId());

        return organizationRepository.save(existing);
    }

    @Transactional
    @Override
    public void changeStatusAndRegister(Long id,
                                        OrganizationStatus status) {

        Organization existing = findById(id);

        existing.setStatus(status);
        existing.setRegisteredAt(LocalDateTime.now());

        //todo: send email in case of status of organization
        organizationRepository.save(existing);
    }

    @Transactional
    @Override
    public void uploadLogo(Long id, LogoDTO logoDTO) {

        Organization existing = findById(id);

        String fileName = logoClient.upload(logoDTO.getFile());

        existing.setLogo(fileName);
        organizationRepository.save(existing);
    }

    @Override
    public Long getOwnerId(Long organizationId) {

        Organization existing = findById(organizationId);

        return existing.getOwnerId();
    }

    private Organization findById(Long id) {

        return organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(
                        "Organization not found",
                        HttpStatus.NOT_FOUND
                ));
    }

    private void sendRegistrationRequestToAdmin(Long organizationId) {

        RegistrationRequestDTO requestDTO = RegistrationRequestDTO.builder()
                .objectToRegisterId(organizationId)
                .objectType(ObjectType.ORGANIZATION)
                .build();

        kafkaTemplate.send(adminTopic, requestDTO);
    }
}





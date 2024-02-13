package com.ryazancev.organization.service.impl;

import com.ryazancev.clients.LogoClient;
import com.ryazancev.dto.admin.ObjectType;
import com.ryazancev.dto.admin.RegistrationRequestDto;
import com.ryazancev.dto.logo.LogoDTO;
import com.ryazancev.organization.kafka.OrganizationProducerService;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.exception.custom.DeletedOrganizationException;
import com.ryazancev.organization.util.exception.custom.OrganizationCreationException;
import com.ryazancev.organization.util.exception.custom.OrganizationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
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

    private final OrganizationProducerService organizationProducerService;


    @Override
    @Cacheable(value = "Organization::getAll")
    public List<Organization> getAll() {

        return organizationRepository.findAll();
    }

    @Override
    @Cacheable(
            value = "Organization::getById",
            key = "#id"
    )
    public Organization getById(Long id, boolean statusCheck) {
        if (statusCheck) {
            return findByIdWithStatusChecks(id);
        } else {
            return findById(id);
        }
    }

    @Transactional
    @Override
    @CacheEvict(
            value = "Organization::getAll",
            allEntries = true
    )
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
    @Caching(
            evict = {
                    @CacheEvict(
                            value = "Organization::getAll",
                            allEntries = true
                    )},
            put = {
                    @CachePut(
                            value = "Organization::getById",
                            key = "#organization.id"
                    )}
    )
    public Organization update(Organization organization) {

        Organization existing = findByIdWithStatusChecks(organization.getId());

        existing.setName(organization.getName());
        existing.setDescription(organization.getDescription());
        existing.setOwnerId(organization.getOwnerId());

        return organizationRepository.save(existing);
    }

    @Transactional
    @Override
    @Caching(
            evict = {
                    @CacheEvict(
                            value = "Organization::getAll",
                            allEntries = true
                    ),
                    @CacheEvict(
                            value = "Organization::getById",
                            key = "#id"
                    )}
    )
    public void changeStatus(Long id,
                             OrganizationStatus status) {

        Organization existing = findByIdWithStatusChecks(id);

        existing.setStatus(status);

        organizationRepository.save(existing);
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            value = "Organization::getAll",
                            allEntries = true
                    ),
                    @CacheEvict(
                            value = "Organization::getById",
                            key = "#id"
                    )}
    )
    public void register(Long id) {

        Organization existing = findByIdWithStatusChecks(id);

        existing.setRegisteredAt(LocalDateTime.now());
        //todo: send email in case of status of organization
        organizationRepository.save(existing);
    }

    @Transactional
    @Override
    @CacheEvict(
            value = "Organization::getById",
            key = "#id"
    )
    public void uploadLogo(Long id, LogoDTO logoDTO) {

        Organization existing = findByIdWithStatusChecks(id);

        String fileName = logoClient.upload(logoDTO.getFile());

        existing.setLogo(fileName);
        organizationRepository.save(existing);
    }

    @Override
    @Cacheable(
            value = "Organization::getOwnerId",
            key = "#organizationId"
    )
    public Long getOwnerId(Long organizationId) {

        Organization existing = findByIdWithStatusChecks(organizationId);

        return existing.getOwnerId();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(
                    value = "Organization::getById",
                    key = "#id"
            ),
            @CacheEvict(
                    value = "Organization::getAll",
                    allEntries = true
            ),
            @CacheEvict(
                    value = "Organization::getOwnerId",
                    key = "#id"
            )
    })
    public String markOrganizationAsDeleted(Long id) {

        Organization found = findByIdWithStatusChecks(id);

        found.setLogo("DELETED");
        found.setDescription("DELETED");
        found.setStatus(OrganizationStatus.DELETED);

        organizationProducerService.sendMessageToProductTopic(id);
        organizationRepository.save(found);

        return "Organization successfully deleted";
    }

    private Organization findByIdWithStatusChecks(Long id) {

        Organization found = findById(id);

        if (found.getStatus().equals(OrganizationStatus.DELETED)) {

            throw new DeletedOrganizationException(
                    "Can not get deleted organization",
                    HttpStatus.BAD_REQUEST);
        }

        //todo: add check if organization is frozen
        return found;
    }

    private Organization findById(Long id) {

        return organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(
                        "Organization not found",
                        HttpStatus.NOT_FOUND
                ));
    }

    private void sendRegistrationRequestToAdmin(Long organizationId) {

        RegistrationRequestDto requestDTO = RegistrationRequestDto.builder()
                .objectToRegisterId(organizationId)
                .objectType(ObjectType.ORGANIZATION)
                .build();

        organizationProducerService.sendMessageToAdminTopic(requestDTO);
    }
}





package com.ryazancev.organization.service.impl;

import com.ryazancev.common.dto.logo.LogoDto;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.ClientsService;
import com.ryazancev.organization.service.OrganizationService;
import com.ryazancev.organization.util.exception.custom.OrganizationNotFoundException;
import com.ryazancev.organization.util.processor.KafkaMessageProcessor;
import com.ryazancev.organization.util.validator.OrganizationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationValidator organizationValidator;
    private final KafkaMessageProcessor kafkaMessageProcessor;

    private final ClientsService clientsService;
    private final MessageSource messageSource;

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
        Organization existing = findById(id);

        if (statusCheck) {
            organizationValidator
                    .validateStatus(existing, OrganizationStatus.INACTIVE);
            organizationValidator
                    .validateStatus(existing, OrganizationStatus.DELETED);
        }

        return existing;
    }

    @Transactional
    @Override
    @CacheEvict(
            value = "Organization::getAll",
            allEntries = true
    )
    public Organization makeRegistrationRequest(
            Organization organization) {

        organizationValidator.validateNameUniqueness(organization);
        organizationValidator.validateDescriptionUniqueness(organization);

        organization.setStatus(OrganizationStatus.INACTIVE);

        Organization saved = organizationRepository.save(organization);

        kafkaMessageProcessor.sendRegistrationRequestToAdmin(saved.getId());

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

        Organization existing = findById(organization.getId());

        organizationValidator.validateAllStatus(existing);

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

        Organization existing = findById(id);

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

        Organization existing = findById(id);

        existing.setRegisteredAt(LocalDateTime.now());

        organizationRepository.save(existing);
    }

    @Transactional
    @Override
    @CacheEvict(
            value = "Organization::getById",
            key = "#id"
    )
    public String uploadLogo(Long id, LogoDto logoDto) {

        Organization existing = findById(id);

        organizationValidator.validateAllStatus(existing);

        String fileName = (String) clientsService
                .uploadLogo(logoDto.getFile());

        existing.setLogo(fileName);
        organizationRepository.save(existing);

        return messageSource.getMessage(
                "service.organization.logo_uploaded",
                new Object[]{
                        id,
                        fileName
                },
                Locale.getDefault()
        );
    }

    @Override
    @Cacheable(
            value = "Organization::getOwnerId",
            key = "#organizationId"
    )
    public Long getOwnerId(Long organizationId) {

        Organization existing = findById(organizationId);

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

        Organization existing = findById(id);

        organizationValidator.validateAllStatus(existing);

        existing.setLogo("DELETED");
        existing.setDescription("DELETED");
        existing.setStatus(OrganizationStatus.DELETED);

        kafkaMessageProcessor
                .sendProductIdToDeleteProductTopic(id);

        organizationRepository.save(existing);

        return messageSource.getMessage(
                "service.organization.deleted",
                new Object[]{id},
                Locale.getDefault()
        );
    }

    private Organization findById(Long id) {

        return organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(
                        messageSource.getMessage(
                                "exception.organization.not_found",
                                new Object[]{id},
                                Locale.getDefault()
                        ),
                        HttpStatus.NOT_FOUND
                ));
    }

}





package com.ryazancev.admin.service.impl;

import com.ryazancev.admin.kafka.AdminProducerService;
import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.repository.RegistrationRequestRepository;
import com.ryazancev.admin.service.RegistrationRequestService;
import com.ryazancev.admin.util.exception.custom.RequestNotFoundException;
import com.ryazancev.dto.admin.ObjectType;
import com.ryazancev.dto.admin.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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
public class RegistrationRequestServiceImpl
        implements RegistrationRequestService {

    private final RegistrationRequestRepository registrationRequestRepository;
    private final AdminProducerService adminProducerService;

    @Override
    @Cacheable(value = "Admin::getAll")
    public List<RegistrationRequest> getAll() {

        return registrationRequestRepository.findAll();
    }

    @Override
    @Cacheable(value = "Admin::getProductRegRequests")
    public List<RegistrationRequest> getProductRegistrationRequests() {

        return registrationRequestRepository
                .findAllByObjectType(ObjectType.PRODUCT);
    }

    @Override
    @Cacheable(value = "Admin::getOrganizationRegRequests")
    public List<RegistrationRequest> getOrganizationRegistrationRequests() {

        return registrationRequestRepository
                .findAllByObjectType(ObjectType.ORGANIZATION);
    }

    @Transactional
    @Override
    @Caching(
            evict = {
                    @CacheEvict(
                            value = "Admin::getAll",
                            allEntries = true),
                    @CacheEvict(
                            value = "Admin::getProductRegRequests",
                            allEntries = true),
                    @CacheEvict(
                            value = "Admin::getOrganizationRegRequests",
                            allEntries = true)
            }
    )
    public RegistrationRequest changeStatus(Long requestId,
                                            RequestStatus status) {

        RegistrationRequest existing =
                registrationRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new RequestNotFoundException(
                                        "Request not found with this id",
                                        HttpStatus.NOT_FOUND
                                ));
        existing.setStatus(status);
        existing.setReviewedAt(LocalDateTime.now());

        RegistrationRequest updated =
                registrationRequestRepository.save(existing);

        adminProducerService.sendResponse(updated);

        //todo: send notification to user about admin's decision

        return updated;
    }


    @Transactional
    @Override
    @Caching(
            evict = {
                    @CacheEvict(
                            value = "Admin::getAll",
                            allEntries = true),
                    @CacheEvict(
                            value = "Admin::getProductRegRequests",
                            allEntries = true),
                    @CacheEvict(
                            value = "Admin::getOrganizationRegRequests",
                            allEntries = true)
            }
    )
    public void create(RegistrationRequest registrationRequest) {

        registrationRequest.setCreatedAt(LocalDateTime.now());
        registrationRequest.setStatus(RequestStatus.ON_REVIEW);
        //todo:send notification to Admin that new request here
        registrationRequestRepository.save(registrationRequest);
    }
}


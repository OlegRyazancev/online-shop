package com.ryazancev.admin.service.impl;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.repository.AdminRepository;
import com.ryazancev.admin.service.AdminService;
import com.ryazancev.admin.util.exception.CustomExceptionFactory;
import com.ryazancev.admin.util.exception.custom.RequestNotFoundException;
import com.ryazancev.admin.util.processor.KafkaMessageProcessor;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.UserLockRequest;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.admin.enums.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final KafkaMessageProcessor kafkaMessageProcessor;

    private final MessageSource messageSource;

    @Override
    @Cacheable(value = "Admin::getAll")
    public List<RegistrationRequest> getAll() {

        return adminRepository.findAll();
    }

    @Override
    @Cacheable(value = "Admin::getProductRegRequests")
    public List<RegistrationRequest> getProductRegistrationRequests() {

        return adminRepository
                .findAllByObjectType(ObjectType.PRODUCT);
    }

    @Override
    @Cacheable(value = "Admin::getOrganizationRegRequests")
    public List<RegistrationRequest> getOrganizationRegistrationRequests() {

        return adminRepository
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
    public RegistrationRequest changeRegistrationStatus(
            final Long requestId,
            final RequestStatus status) {

        RegistrationRequest existing =
                adminRepository.findById(requestId)
                        .orElseThrow(() ->
                                CustomExceptionFactory
                                        .getRequestNotFound()
                                        .byId(
                                                messageSource,
                                                String.valueOf(requestId)
                                        )
                        );
        existing.setStatus(status);
        existing.setReviewedAt(LocalDateTime.now());

        RegistrationRequest updated =
                adminRepository.save(existing);

        kafkaMessageProcessor.sendRegisterResponse(updated);
        kafkaMessageProcessor.sendPrivateNotification(updated);

        if (updated.getObjectType() == ObjectType.PRODUCT
                && updated.getStatus() == RequestStatus.ACCEPTED) {

            kafkaMessageProcessor.sendPublicNotification(updated);
        }

        return updated;
    }

    @Override
    public String changeObjectStatus(
            final ObjectRequest request) {

        kafkaMessageProcessor.sendMessageToChangeObjectStatus(request);
        kafkaMessageProcessor.sendPrivateNotification(request);

        return messageSource.getMessage(
                "service.admin.change_status_request",
                new Object[]{
                        request.getObjectStatus().name(),
                        request.getObjectType().name(),
                        request.getObjectId()
                },
                Locale.getDefault()
        );
    }

    @Override
    public String toggleUserLock(
            final UserLockRequest request) {

        kafkaMessageProcessor.sendMessageToToggleUserLock(request);
        kafkaMessageProcessor.sendPrivateNotification(request);

        return messageSource.getMessage(
                "service.admin.toggle_account_lock_request",
                new Object[]{
                        request.isLock(),
                        request.getUserId()
                },
                Locale.getDefault()
        );
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
    public RegistrationRequest create(
            final RegistrationRequest registrationRequest) {

        registrationRequest.setCreatedAt(LocalDateTime.now());
        registrationRequest.setStatus(RequestStatus.ON_REVIEW);

        return adminRepository.save(registrationRequest);
    }
}

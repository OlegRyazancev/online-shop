package com.ryazancev.admin.service.impl;

import com.ryazancev.admin.kafka.AdminProducerService;
import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.repository.AdminRepository;
import com.ryazancev.admin.service.AdminService;
import com.ryazancev.admin.util.exception.custom.RequestNotFoundException;
import com.ryazancev.admin.util.notification.NotificationProcessor;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.UserLockRequest;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.admin.enums.RequestStatus;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
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
    private final AdminProducerService adminProducerService;
    private final NotificationProcessor notificationProcessor;

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
    public RegistrationRequest changeRegistrationStatus(Long requestId,
                                                        RequestStatus status) {

        RegistrationRequest existing =
                adminRepository.findById(requestId)
                        .orElseThrow(() -> new RequestNotFoundException(
                                messageSource.getMessage(
                                        "request_not_found_by_id",
                                        new Object[]{requestId},
                                        Locale.getDefault()
                                ),
                                HttpStatus.NOT_FOUND));
        existing.setStatus(status);
        existing.setReviewedAt(LocalDateTime.now());

        RegistrationRequest updated =
                adminRepository.save(existing);

        adminProducerService.sendRegisterResponse(updated);


        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                updated,
                                NotificationScope.PRIVATE
                        );

        adminProducerService.sendNotification(privateNotificationRequest);

        if (updated.getObjectType() == ObjectType.PRODUCT
                && updated.getStatus() == RequestStatus.ACCEPTED) {

            NotificationRequest publicNotificationRequest =
                    notificationProcessor
                            .createNotification(
                                    updated,
                                    NotificationScope.PUBLIC
                            );

            adminProducerService.sendNotification(publicNotificationRequest);
        }

        return updated;
    }

    @Override
    public String changeObjectStatus(ObjectRequest request) {

        adminProducerService.sendMessageToChangeObjectStatus(request);

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PRIVATE
                        );

        adminProducerService.sendNotification(privateNotificationRequest);

        return messageSource.getMessage(
                "change_object_status_request",
                new Object[]{
                        request.getObjectStatus().name(),
                        request.getObjectType().name(),
                        request.getObjectId()
                },
                Locale.getDefault()
        );
    }

    @Override
    public String toggleUserLock(UserLockRequest request) {

        adminProducerService.sendMessageToToggleUserLock(request);

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PRIVATE
                        );

        adminProducerService.sendNotification(privateNotificationRequest);

        return messageSource.getMessage(
                "toggle_account_lock_request",
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
    public RegistrationRequest create(RegistrationRequest registrationRequest) {

        registrationRequest.setCreatedAt(LocalDateTime.now());
        registrationRequest.setStatus(RequestStatus.ON_REVIEW);

        return adminRepository.save(registrationRequest);
    }
}


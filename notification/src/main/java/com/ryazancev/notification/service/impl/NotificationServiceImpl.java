package com.ryazancev.notification.service.impl;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationStatus;
import com.ryazancev.notification.model.notification.AdminNotification;
import com.ryazancev.notification.model.notification.Notification;
import com.ryazancev.notification.model.notification.PrivateNotification;
import com.ryazancev.notification.model.notification.PublicNotification;
import com.ryazancev.notification.repository.AdminNotificationRepository;
import com.ryazancev.notification.repository.PrivateNotificationRepository;
import com.ryazancev.notification.repository.PublicNotificationRepository;
import com.ryazancev.notification.service.NotificationService;
import com.ryazancev.notification.util.NotificationUtil;
import com.ryazancev.notification.util.exception.CustomExceptionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Oleg Ryazancev
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final AdminNotificationRepository adminRepository;
    private final PrivateNotificationRepository privateRepository;
    private final PublicNotificationRepository publicRepository;
    private final NotificationUtil notificationUtil;

    private final MessageSource messageSource;


    @Override
    public List<Notification> getNotificationsByRecipientId(
            final Long customerId,
            final NotificationScope castedScope) {

        switch (castedScope) {
            case PUBLIC -> {

                return publicRepository
                        .findAll()
                        .stream()
                        .map(pn -> (Notification) pn)
                        .toList();
            }
            case PRIVATE -> {

                return privateRepository
                        .findByRecipientId(customerId)
                        .stream()
                        .map(pn -> (Notification) pn)
                        .toList();
            }
            case ADMIN -> {

                return adminRepository
                        .findAll()
                        .stream()
                        .map(an -> (Notification) an)
                        .toList();
            }
            default -> {
            }
        }

        return null;
    }

    @Override
    public Notification getById(final String id,
                                final NotificationScope castedScope) {

        switch (castedScope) {

            case PRIVATE -> {

                PrivateNotification notification = privateRepository
                        .findById(id)
                        .orElseThrow(() ->
                                CustomExceptionFactory
                                        .getNotificationNotFound()
                                        .byId(
                                                messageSource,
                                                castedScope,
                                                id
                                        ));
                if (notification.getStatus() == NotificationStatus.UNREAD) {

                    notification.setStatus(NotificationStatus.READ);
                    privateRepository.save(notification);
                }

                return notification;
            }
            case PUBLIC -> {

                return publicRepository.findById(id)
                        .orElseThrow(() ->
                                CustomExceptionFactory
                                        .getNotificationNotFound()
                                        .byId(
                                                messageSource,
                                                castedScope,
                                                id
                                        ));
            }
            case ADMIN -> {

                return adminRepository.findById(id)
                        .orElseThrow(() ->
                                CustomExceptionFactory
                                        .getNotificationNotFound()
                                        .byId(
                                                messageSource,
                                                castedScope,
                                                id
                                        ));
            }
            default -> {
            }
        }
        return null;
    }

    @Override
    public AdminNotification createAdminNotification(
            final NotificationRequest request) {

        AdminNotification notification =
                notificationUtil.buildNotification(
                        request, AdminNotification.class);

        return adminRepository.insert(notification);
    }

    @Override
    public PublicNotification createPublicNotification(
            final NotificationRequest request) {

        PublicNotification notification =
                notificationUtil.buildNotification(
                        request, PublicNotification.class);

        return publicRepository.insert(notification);
    }

    @Override
    public PrivateNotification createPrivateNotification(
            final NotificationRequest request) {

        PrivateNotification notification =
                notificationUtil.buildNotification(
                        request, PrivateNotification.class);

        return privateRepository.insert(notification);
    }

    @Override
    public Long getRecipientIdByPrivateNotificationId(final String id) {

        PrivateNotification notification =
                privateRepository.findById(id)
                        .orElseThrow(() ->
                                CustomExceptionFactory
                                        .getNotificationNotFound()
                                        .byId(
                                                messageSource,
                                                NotificationScope.PRIVATE,
                                                id
                                        ));

        return notification.getRecipientId();
    }
}

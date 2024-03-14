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
import com.ryazancev.notification.util.exception.custom.NotificationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;


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
            Long customerId, NotificationScope castedScope) {

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
        }

        return null;
    }

    @Override
    public Notification getById(String id, NotificationScope castedScope) {

        switch (castedScope) {

            case PRIVATE -> {

                PrivateNotification notification = privateRepository
                        .findById(id)
                        .orElseThrow(() -> new NotificationNotFoundException(
                                messageSource.getMessage(
                                        "notification_by_id_not_found",
                                        new Object[]{castedScope, id},
                                        Locale.getDefault()
                                ),
                                HttpStatus.BAD_REQUEST));

                if (notification.getStatus() == NotificationStatus.UNREAD) {

                    notification.setStatus(NotificationStatus.READ);
                    privateRepository.save(notification);
                }

                return notification;
            }
            case PUBLIC -> {

                return publicRepository.findById(id)
                        .orElseThrow(() -> new NotificationNotFoundException(
                                messageSource.getMessage(
                                        "notification_by_id_not_found",
                                        new Object[]{castedScope, id},
                                        Locale.getDefault()
                                ),
                                HttpStatus.BAD_REQUEST));
            }
            case ADMIN -> {

                return adminRepository.findById(id)
                        .orElseThrow(() -> new NotificationNotFoundException(
                                messageSource.getMessage(
                                        "notification_by_id_not_found",
                                        new Object[]{castedScope, id},
                                        Locale.getDefault()
                                ),
                                HttpStatus.BAD_REQUEST
                        ));
            }
        }
        return null;
    }

    @Override
    public AdminNotification createAdminNotification(
            NotificationRequest request) {

        AdminNotification notification =
                notificationUtil.buildNotification(
                        request, AdminNotification.class);

        return adminRepository.insert(notification);
    }

    @Override
    public PublicNotification createPublicNotification(
            NotificationRequest request) {

        PublicNotification notification =
                notificationUtil.buildNotification(
                        request, PublicNotification.class);

        return publicRepository.insert(notification);
    }

    @Override
    public PrivateNotification createPrivateNotification(
            NotificationRequest request) {

        PrivateNotification notification =
                notificationUtil.buildNotification(
                        request, PrivateNotification.class);

        return privateRepository.insert(notification);
    }

    @Override
    public Long getRecipientIdByPrivateNotificationId(String id) {

        PrivateNotification notification =
                privateRepository.findById(id)
                        .orElseThrow(() -> new NotificationNotFoundException(
                                messageSource.getMessage(
                                        "notification_by_id_not_found",
                                        new Object[]{
                                                NotificationScope.PRIVATE,
                                                id
                                        },
                                        Locale.getDefault()
                                ),
                                HttpStatus.BAD_REQUEST
                        ));

        return notification.getRecipientId();
    }
}

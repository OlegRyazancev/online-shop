package com.ryazancev.notification.service.impl;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationStatus;
import com.ryazancev.notification.model.Content;
import com.ryazancev.notification.model.notification.Notification;
import com.ryazancev.notification.model.notification.PrivateNotification;
import com.ryazancev.notification.model.notification.PublicNotification;
import com.ryazancev.notification.repository.PrivateNotificationRepository;
import com.ryazancev.notification.repository.PublicNotificationRepository;
import com.ryazancev.notification.service.ContentService;
import com.ryazancev.notification.service.NotificationService;
import com.ryazancev.notification.util.exception.custom.NotificationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;



/**
 * @author Oleg Ryazancev
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final PrivateNotificationRepository privateNotificationRepository;
    private final PublicNotificationRepository publicNotificationRepository;

    private final ContentService contentService;
    private final MessageSource messageSource;


    @Override
    public List<Notification> getNotificationsByRecipientId(
            Long customerId, NotificationScope castedScope) {

        switch (castedScope) {
            case PUBLIC -> {

                return publicNotificationRepository
                        .findAll()
                        .stream()
                        .map(pn -> (Notification) pn)
                        .toList();
            }
            case PRIVATE -> {

                return privateNotificationRepository
                        .findByRecipientId(customerId)
                        .stream()
                        .map(pn -> (Notification) pn)
                        .toList();
            }
        }

        return null;
    }

    @Override
    public Notification getById(String id, NotificationScope castedScope) {

        switch (castedScope) {

            case PRIVATE -> {

                return privateNotificationRepository.findById(id)
                        .orElseThrow(() -> new NotificationNotFoundException(
                                messageSource.getMessage(
                                        "private_by_id_not_found",
                                        new Object[]{id},
                                        Locale.getDefault()
                                ),
                                HttpStatus.BAD_REQUEST));
            }
            case PUBLIC -> {

                return publicNotificationRepository.findById(id)
                        .orElseThrow(() -> new NotificationNotFoundException(
                                messageSource.getMessage(
                                        "public_by_id_not_found",
                                        new Object[]{id},
                                        Locale.getDefault()
                                ),
                                HttpStatus.BAD_REQUEST));
            }
        }
        return null;
    }

    @Override
    public PublicNotification createPublicNotification(
            NotificationRequest request) {

        PublicNotification notification =
                (PublicNotification) buildNotification(request);

        return publicNotificationRepository.insert(notification);
    }

    @Override
    public PrivateNotification createPrivateNotification(
            NotificationRequest request) {

        PrivateNotification notification =
                (PrivateNotification) buildNotification(request);
        notification.setRecipientId(request.getRecipientId());

        return privateNotificationRepository.insert(notification);
    }


    private Notification buildNotification(NotificationRequest request) {

        Content content = contentService.generateContent(
                request.getType(),
                request.getProperties());


        return Notification.builder()
                .senderId(request.getSenderId())
                .content(content)
                .timestamp(LocalDateTime.now())
                .status(NotificationStatus.UNREAD)
                .build();
    }
}

package com.ryazancev.notification.controller;

import com.ryazancev.common.dto.notification.NotificationDto;
import com.ryazancev.common.dto.notification.NotificationsSimpleResponse;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.notification.model.notification.Notification;
import com.ryazancev.notification.service.NotificationService;
import com.ryazancev.notification.util.NotificationUtil;
import com.ryazancev.notification.util.processor.DtoProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationUtil notificationUtil;
    private final DtoProcessor dtoProcessor;

    @GetMapping("/customer/{id}")
    public NotificationsSimpleResponse getNotificationsByRecipientId(
            @PathVariable("id") final Long customerId,
            @RequestParam("scope") final String scope) {

        NotificationScope scopeEnum = notificationUtil.castScope(scope);

        List<Notification> notifications =
                notificationService.getNotificationsByRecipientId(
                        customerId,
                        scopeEnum
                );

        return dtoProcessor.createNotificationsResponse(notifications);
    }


    @GetMapping("/{id}")
    public NotificationDto getNotificationById(
            @PathVariable("id") final String id,
            @RequestParam("scope") final String scope) {

        NotificationScope scopeEnum = notificationUtil.castScope(scope);

        Notification notification =
                notificationService.getById(id, scopeEnum);

        return dtoProcessor.createNotificationDto(notification);
    }

    @GetMapping("/private/{id}/recipient-id")
    public Long getRecipientIdByPrivateNotificationId(
            @PathVariable("id") final String id) {

        return notificationService.getRecipientIdByPrivateNotificationId(id);
    }
}

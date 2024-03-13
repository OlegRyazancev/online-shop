package com.ryazancev.notification.controller;

import com.ryazancev.common.dto.notification.NotificationDto;
import com.ryazancev.common.dto.notification.NotificationsSimpleResponse;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.notification.model.notification.Notification;
import com.ryazancev.notification.service.NotificationService;
import com.ryazancev.notification.util.DtoProcessor;
import com.ryazancev.notification.util.NotificationUtil;
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
            @PathVariable("id") Long customerId,
            @RequestParam("scope") String scope) {

        NotificationScope scopeEnum = notificationUtil.castScope(scope);

        List<Notification> notifications =
                notificationService.getNotificationsByRecipientId(
                        customerId,
                        scopeEnum
                );

        return dtoProcessor.createNotificationsResponse(notifications);
    }


    @GetMapping("/{id}")
    private NotificationDto getNotificationById(
            @PathVariable("id") String id,
            @RequestParam("scope") String scope) {

        NotificationScope scopeEnum = notificationUtil.castScope(scope);

        Notification notification =
                notificationService.getById(id, scopeEnum);

        return dtoProcessor.createNotificationDto(notification);

    }


}

package com.ryazancev.notification.util.processor;

import com.ryazancev.common.dto.notification.NotificationDto;
import com.ryazancev.common.dto.notification.NotificationsSimpleResponse;
import com.ryazancev.notification.model.notification.Notification;
import com.ryazancev.notification.service.ClientsService;
import com.ryazancev.notification.util.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class DtoProcessor {

    private final NotificationMapper notificationMapper;
    private final ClientsService clientsService;

    public NotificationDto createNotificationDto(Notification notification) {

        NotificationDto notificationDto =
                notificationMapper.toDto(notification);

        notificationDto.setSender(
                clientsService.getSimpleCustomerById(
                        notification.getSenderId()));

        return notificationDto;
    }

    public NotificationsSimpleResponse createNotificationsResponse(
            List<Notification> notifications) {

        List<NotificationDto> notificationsDto = Collections.emptyList();

        if (!notifications.isEmpty()) {

            notificationsDto = notificationMapper.toListDto(notifications);

            for (int i = 0; i < notifications.size(); i++) {

                notificationsDto.get(i).setSender(
                        clientsService.getSimpleCustomerById(
                                notifications.get(i).getSenderId()));
            }
        }

        return NotificationsSimpleResponse.builder()
                .notifications(notificationsDto)
                .build();
    }
}

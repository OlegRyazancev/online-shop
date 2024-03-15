package com.ryazancev.common.clients;

import com.ryazancev.common.config.FeignClientsConfiguration;
import com.ryazancev.common.dto.notification.NotificationDto;
import com.ryazancev.common.dto.notification.NotificationsSimpleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author Oleg Ryazancev
 */

@FeignClient(
        name = "notification",
        configuration = FeignClientsConfiguration.class,
        url = "${clients.notification.url}"
)
public interface NotificationClient {

    @GetMapping("api/v1/notifications/customer/{id}")
    NotificationsSimpleResponse getNotificationsByRecipientId(
            @PathVariable("id") Long customerId,
            @RequestParam("scope") String scope);

    @GetMapping("api/v1/notifications/{id}")
    NotificationDto getNotificationById(
            @PathVariable("id") String id,
            @RequestParam("scope") String scope);

    @GetMapping("api/v1/notifications/private/{id}/recipient-id")
    Long getRecipientIdByPrivateNotificationId(
            @PathVariable("id") String id);
}

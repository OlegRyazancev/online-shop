package com.ryazancev.common.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response containing a list of notifications")
public class NotificationsSimpleResponse {

    private List<NotificationDto> notifications;
}

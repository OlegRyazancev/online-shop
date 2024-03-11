package com.ryazancev.common.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author Oleg Ryazancev
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Notification request")
public class NotificationRequest {

    private Long recipientId;

    private Long senderId;

    private NotificationType type;

    private NotificationScope scope;
}

package com.ryazancev.common.dto.notification;

import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Properties;

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

    private Properties properties;
}

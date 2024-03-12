package com.ryazancev.notification.model.notification;

import com.ryazancev.common.dto.notification.enums.NotificationStatus;
import com.ryazancev.notification.model.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements Serializable {

    @Id
    private String id;
    private Content content;
    private LocalDateTime timestamp;
    private Long senderId;
    private NotificationStatus status;

}

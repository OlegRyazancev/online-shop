package com.ryazancev.notification.model.notification;

import com.ryazancev.common.dto.notification.enums.NotificationStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author Oleg Ryazancev
 */

@Document(collection = "private-notifications")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PrivateNotification
        extends Notification
        implements Serializable{


    private Long recipientId;

}

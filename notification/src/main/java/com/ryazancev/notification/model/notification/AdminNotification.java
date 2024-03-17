package com.ryazancev.notification.model.notification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author Oleg Ryazancev
 */

@EqualsAndHashCode(callSuper = true)
@Document(collection = "admin-notifications")
@SuperBuilder
@Data
@NoArgsConstructor
public class AdminNotification
        extends Notification
        implements Serializable {
}

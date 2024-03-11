package com.ryazancev.notification.model.notification;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author Oleg Ryazancev
 */

@EqualsAndHashCode(callSuper = true)
@Document(collection = "public-notifications")
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicNotification
        extends Notification
        implements Serializable {

}
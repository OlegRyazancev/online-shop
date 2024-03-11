package com.ryazancev.notification.model;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

public class Notification {

    private Long id;
    private Content content;
    private Long recipientId;
    private Long senderId;
    private NotificationStatus status;
    private LocalDateTime timeStamp;
}

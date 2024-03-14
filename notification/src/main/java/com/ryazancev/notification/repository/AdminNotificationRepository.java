package com.ryazancev.notification.repository;

import com.ryazancev.notification.model.notification.AdminNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Oleg Ryazancev
 */

@Repository
public interface AdminNotificationRepository
        extends MongoRepository<AdminNotification, String> {
}

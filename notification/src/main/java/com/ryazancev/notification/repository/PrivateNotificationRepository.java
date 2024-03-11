package com.ryazancev.notification.repository;

import com.ryazancev.notification.model.notification.PrivateNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Oleg Ryazancev
 */

@Repository
public interface PrivateNotificationRepository
        extends MongoRepository<PrivateNotification, String> {
}

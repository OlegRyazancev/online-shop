package com.ryazancev.notification.repository;

import com.ryazancev.notification.model.notification.PublicNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Oleg Ryazancev
 */

@Repository
public interface PublicNotificationRepository
        extends MongoRepository<PublicNotification, String> {
}

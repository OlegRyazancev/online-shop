package com.ryazancev.auth.kafka;

import com.ryazancev.auth.service.UserService;
import com.ryazancev.common.dto.admin.UserLockRequest;
import com.ryazancev.common.dto.user.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthMessageListener {

    private final UserService userService;

    @KafkaListener(
            topics = "${spring.kafka.topic.user.toggle-lock}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "userLockMessageFactory"
    )
    void toggleUserLock(final UserLockRequest request) {

        log.info("Received message to {} user with id: {}",
                request.isLock() ? "lock" : "unlock",
                request.getUserId());

        try {

            log.debug("Toggling user lock...");
            userService.toggleUserLock(request.getUserId(), request.isLock());

            log.debug("User with id {} was {}ed",
                    request.getUserId(),
                    request.isLock() ? "lock" : "unlock");

        } catch (Exception e) {

            log.error("Failed to {} user with id {}: {}",
                    request.isLock() ? "lock" : "unlock",
                    request.getUserId(),
                    e.getMessage());

        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.user.delete}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "longValueMessageFactory"
    )
    void markUserAsDeleted(final Long customerId) {

        log.info("Received message to mark user as deleted where"
                        + " customerId: {}",
                customerId);

        try {

            log.debug("Marking user as deleted...");
            userService.markUserAsDeletedByCustomerId(customerId);

            log.debug("User with customerId {} was marked as deleted",
                    customerId);

        } catch (Exception e) {

            log.error("Failed to mark user with customerId {} as deleted: {}",
                    customerId,
                    e.getMessage());
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.user.update}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "userUpdateMessageFactory"
    )
    void updateUser(final UserUpdateRequest request) {

        log.info("Received message to update user where customerId: {}",
                request.getCustomerId());

        try {

            log.debug("Updating user...");
            userService.updateByCustomer(request);

            log.debug("User with customerId {} was updated",
                    request.getCustomerId());

        } catch (Exception e) {

            log.error("Failed to update user with customerId {}: {}",
                    request.getCustomerId(),
                    e.getMessage());
        }
    }
}

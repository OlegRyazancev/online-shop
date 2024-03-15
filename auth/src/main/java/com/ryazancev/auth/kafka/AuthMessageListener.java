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
    void toggleUserLock(UserLockRequest request) {

        log.info("Received message to lock: {}, user: {}",
                request.isLock(),
                request.getUserId());
        try {

            log.trace("Toggling user lock...");
            userService.toggleUserLock(request.getUserId(), request.isLock());

            log.debug("User with id {} was locked/unlocked",
                    request.getUserId());

        } catch (Exception e) {

            log.error("User with id {} was not locked/unlocked: {}",
                    request.getUserId(),
                    e.getMessage());
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.user.delete}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "longValueMessageFactory"
    )
    void markUserAsDeleted(Long customerId) {

        log.info("Received message to delete user where customerId: {}",
                customerId);

        try {

            log.trace("Marking user as deleted...");
            userService.markUserAsDeletedByCustomerId(customerId);

            log.debug("User was marked as deleted");

        } catch (Exception e) {

            log.error("User was not marked as deleted: {}", e.getMessage());
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.user.update}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "userUpdateMessageFactory"
    )
    void updateUser(UserUpdateRequest request) {

        log.info("Received message to update user where customerId: {}",
                request.getCustomerId());

        try {

            log.trace("Updating user...");
            userService.updateByCustomer(request);

            log.debug("User was updated");

        } catch (Exception e) {

            log.error("User was not updated: {}", e.getMessage());
        }
    }
}

package com.ryazancev.auth.kafka;

import com.ryazancev.auth.service.UserService;
import com.ryazancev.dto.admin.UserLockRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthMessageListeners {

    private final UserService userService;

    @KafkaListener(
            topics = "${spring.kafka.topic.user.toggle-lock}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "userLockMessageFactory"
    )
    void toggleUserLock(UserLockRequest request) {

        log.info("Received message to lock: {}, user: {}",
                request.isLock(),
                request.getUsername());

        userService.toggleUserLock(request.getUsername(), request.isLock());

        if (request.isLock()) {

            log.info("User was successfully locked");
        } else {

            log.info("User was successfully unlocked");
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

        userService.markUserAsDeletedByCustomerId(customerId);

        log.info("User was successfully marked as deleted");


    }
}

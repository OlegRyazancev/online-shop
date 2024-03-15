package com.ryazancev.admin.kafka;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.service.AdminService;
import com.ryazancev.admin.util.mapper.AdminMapper;
import com.ryazancev.admin.util.notification.NotificationProcessor;
import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
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
public class AdminMessageListeners {


    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final NotificationProcessor notificationProcessor;
    private final AdminProducerService adminProducerService;

    @KafkaListener(
            topics = "${spring.kafka.topic.admin}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void createRegistrationRequest(RegistrationRequestDto requestDto) {

        log.info("Received message to register organization/product with id {}," +
                        "and type: {}",
                requestDto.getObjectToRegisterId(),
                requestDto.getObjectType().name());

        try {

            log.trace("Creating request...");
            RegistrationRequest request =
                    adminMapper.toEntity(requestDto);

            RegistrationRequest created = adminService.create(request);

            log.trace("Creating admin notification...");
            NotificationRequest notificationRequest =
                    notificationProcessor.createNotification(
                            request,
                            NotificationScope.ADMIN);

            log.trace("Sending admin notification...");
            adminProducerService.sendNotification(notificationRequest);

            log.info("Request was created with id: {}",
                    created.getId());

        } catch (Exception e) {

            log.error("Request was not created");
        }

    }
}


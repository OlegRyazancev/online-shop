package com.ryazancev.admin.kafka;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.service.AdminService;
import com.ryazancev.admin.util.mapper.AdminMapper;
import com.ryazancev.admin.util.processor.KafkaMessageProcessor;
import com.ryazancev.common.dto.admin.RegistrationRequestDto;
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
public class AdminMessageListener {


    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final KafkaMessageProcessor kafkaMessageProcessor;

    @KafkaListener(
            topics = "${spring.kafka.topic.admin}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void createRegistrationRequest(RegistrationRequestDto requestDto) {

        log.info("Received message to register {} with id {}",
                requestDto.getObjectType(),
                requestDto.getObjectToRegisterId());
        try {

            log.debug("Creating registration request...");
            RegistrationRequest request =
                    adminMapper.toEntity(requestDto);

            RegistrationRequest created = adminService.create(request);

            log.debug("Sending admin notification...");
            kafkaMessageProcessor.sendAdminNotification(request);

            log.debug("Registration request created successfully with id: {}",
                    created.getId());

        } catch (Exception e) {

            log.error("Failed to create registration request: {}",
                    e.getMessage());
        }

    }
}


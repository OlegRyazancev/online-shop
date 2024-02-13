package com.ryazancev.admin.kafka;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.service.AdminService;
import com.ryazancev.admin.util.mapper.AdminMapper;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminMessageListeners {


    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @KafkaListener(
            topics = "${spring.kafka.topic.admin}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void createRegistrationRequest(RegistrationRequestDTO requestDTO) {

        log.info("Received message to register organization/product with id {}," +
                        "and type: {}",
                requestDTO.getObjectToRegisterId(),
                requestDTO.getObjectType().name());

        log.info("Creating request...");
        RegistrationRequest request =
                adminMapper.toEntity(requestDTO);

        adminService.create(request);

        log.info("Request successfully created");
    }
}


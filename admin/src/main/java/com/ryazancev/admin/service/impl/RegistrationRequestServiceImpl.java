package com.ryazancev.admin.service.impl;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.repository.RegistrationRequestRepository;
import com.ryazancev.admin.service.RegistrationRequestService;
import com.ryazancev.admin.util.mapper.RegistrationRequestMapper;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import com.ryazancev.dto.admin.RegistrationRequestsResponse;
import com.ryazancev.dto.admin.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegistrationRequestServiceImpl implements RegistrationRequestService {

    @Value("${spring.kafka.topic.organization}")
    private String organizationTopic;
    private final KafkaTemplate<String, RegistrationRequestDTO> kafkaTemplate;

    private final RegistrationRequestRepository registrationRequestRepository;
    private final RegistrationRequestMapper registrationRequestMapper;

    @Override
    public RegistrationRequestsResponse getAll() {

        List<RegistrationRequest> requests = registrationRequestRepository.findAll();

        return RegistrationRequestsResponse.builder()
                .requests(registrationRequestMapper.toDtoList(requests))
                .build();
    }

    //todo: GET BY PRODUCT OBJECT TYPE
    //todo: GET BY ORGANIZATION OBJECT TYPE

    @Transactional
    @Override
    public RegistrationRequestDTO changeStatus(Long requestId,
                                               RequestStatus status) {

        RegistrationRequest existing =
                registrationRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Request not found with this id"));
        //todo: add custom exception
        existing.setStatus(status);
        existing.setReviewedAt(LocalDateTime.now());

        RegistrationRequest updated = registrationRequestRepository.save(existing);

        RegistrationRequestDTO requestDTO =
                registrationRequestMapper.toDto(updated);

        sendResponseToOrganization(requestDTO);

        //todo: send notification to user about admin's decision

        return requestDTO;
    }

    private void sendResponseToOrganization(RegistrationRequestDTO requestDTO) {

        switch (requestDTO.getObjectType()) {
            case PRODUCT -> {
                //todo:logic for product topic
            }
            case ORGANIZATION -> {
                kafkaTemplate.send(organizationTopic, requestDTO);
            }
        }

    }

    @Transactional
    @Override
    public void create(RegistrationRequestDTO registrationRequestEitDTO) {

        RegistrationRequest toSave = RegistrationRequest.builder()
                .objectType(registrationRequestEitDTO.getObjectType())
                .objectToRegisterId(
                        registrationRequestEitDTO.getObjectToRegisterId())
                .status(RequestStatus.ON_REVIEW)
                .createdAt(LocalDateTime.now())
                .build();

        registrationRequestRepository.save(toSave);
    }
}


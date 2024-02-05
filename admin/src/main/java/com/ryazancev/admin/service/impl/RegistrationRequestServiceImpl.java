package com.ryazancev.admin.service.impl;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.repository.RegistrationRequestRepository;
import com.ryazancev.admin.service.RegistrationRequestService;
import com.ryazancev.admin.util.exception.custom.RequestNotFoundException;
import com.ryazancev.admin.util.mapper.RegistrationRequestMapper;
import com.ryazancev.dto.admin.ObjectType;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import com.ryazancev.dto.admin.RegistrationRequestsResponse;
import com.ryazancev.dto.admin.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    @Value("${spring.kafka.topic.organization.register}")
    private String organizationTopic;

    @Value("${spring.kafka.topic.product.register}")
    private String productTopic;

    private final KafkaTemplate<String, RegistrationRequestDTO> kafkaTemplate;

    private final RegistrationRequestRepository registrationRequestRepository;
    private final RegistrationRequestMapper registrationRequestMapper;

    @Override
    public RegistrationRequestsResponse getAll() {

        List<RegistrationRequest> requests =
                registrationRequestRepository.findAll();

        return RegistrationRequestsResponse.builder()
                .requests(registrationRequestMapper.toDtoList(requests))
                .build();
    }

    @Override
    public RegistrationRequestsResponse getProductRegistrationRequests() {

        List<RegistrationRequest> requests =
                registrationRequestRepository
                        .findAllByObjectType(ObjectType.PRODUCT);

        return RegistrationRequestsResponse.builder()
                .requests(registrationRequestMapper.toDtoList(requests))
                .build();
    }

    @Override
    public RegistrationRequestsResponse getOrganizationRegistrationRequests() {

        List<RegistrationRequest> requests =
                registrationRequestRepository
                        .findAllByObjectType(ObjectType.ORGANIZATION);

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
                                new RequestNotFoundException(
                                        "Request not found with this id",
                                        HttpStatus.NOT_FOUND
                                ));
        existing.setStatus(status);
        existing.setReviewedAt(LocalDateTime.now());

        RegistrationRequest updated =
                registrationRequestRepository.save(existing);

        RegistrationRequestDTO requestDTO =
                registrationRequestMapper.toDto(updated);

        sendResponse(requestDTO);

        //todo: send notification to user about admin's decision

        return requestDTO;
    }

    private void sendResponse(RegistrationRequestDTO requestDTO) {

        switch (requestDTO.getObjectType()) {
            case PRODUCT -> {
                kafkaTemplate.send(productTopic, requestDTO);
                log.info("Request sent to product topic with: {} " +
                                "and status {}",
                        requestDTO.getObjectType(),
                        requestDTO.getStatus());
            }
            case ORGANIZATION -> {
                kafkaTemplate.send(organizationTopic, requestDTO);
                log.info("Request sent to organization topic with: {} " +
                                "and status {}",
                        requestDTO.getObjectType(),
                        requestDTO.getStatus());
            }
            default -> {
                log.info("Unknown request/object type: {}",
                        requestDTO.getObjectType());
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


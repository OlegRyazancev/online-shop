package com.ryazancev.admin.service.impl;

import com.ryazancev.admin.dto.OrgRegRequestDTO;
import com.ryazancev.admin.dto.OrgRegRequestsResponse;
import com.ryazancev.admin.model.OrgRegRequest;
import com.ryazancev.admin.model.RequestStatus;
import com.ryazancev.admin.repository.OrgRegRequestRepository;
import com.ryazancev.admin.service.OrgRegRequestService;
import com.ryazancev.admin.util.mapper.OrgRegRequestMapper;
import com.ryazancev.dto.admin.RegistrationResponse;
import com.ryazancev.dto.admin.ResponseStatus;
import jakarta.ws.rs.NotFoundException;
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
public class OrgRegRequestServiceImpl implements OrgRegRequestService {

    private final OrgRegRequestRepository orgRegRequestRepository;
    private final OrgRegRequestMapper orgRegRequestMapper;

    @Value("${spring.kafka.topic.organization}")
    private String organizationTopic;
    private final KafkaTemplate<String, RegistrationResponse> kafkaTemplate;

    @Override
    public OrgRegRequestsResponse getAll() {

        List<OrgRegRequest> requests = orgRegRequestRepository.findAll();

        return OrgRegRequestsResponse.builder()
                .requests(orgRegRequestMapper.toDtoList(requests))
                .build();
    }

    @Transactional
    @Override
    public OrgRegRequestDTO changeStatus(Long requestId,
                                         RequestStatus status) {

        OrgRegRequest existing =
                orgRegRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Request not found with this id"));

        existing.setStatus(status);
        existing.setReviewedAt(LocalDateTime.now());

        OrgRegRequest updated = orgRegRequestRepository.save(existing);

        sendResponseToOrganization(status, existing.getOrganizationId());

        //todo: send notification to user about admin's decision
        return orgRegRequestMapper.toDto(updated);
    }

    private void sendResponseToOrganization(RequestStatus status, Long organizationId) {

        RegistrationResponse response = RegistrationResponse.builder()
                .objectToBeRegisteredId(organizationId)
                .build();

        if (status.equals(RequestStatus.ACCEPTED)) {
            response.setStatus(ResponseStatus.ACCEPTED);
        } else {
            response.setStatus(ResponseStatus.REJECTED);
        }

        kafkaTemplate.send(organizationTopic, response);
    }

    @Transactional
    @Override
    public void create(Long organizationId) {

        OrgRegRequest toSave = OrgRegRequest.builder()
                .organizationId(organizationId)
                .status(RequestStatus.ON_REVIEW)
                .createdAt(LocalDateTime.now())
                .build();

        orgRegRequestRepository.save(toSave);
    }
}


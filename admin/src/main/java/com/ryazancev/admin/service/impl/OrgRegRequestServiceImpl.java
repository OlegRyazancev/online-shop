package com.ryazancev.admin.service.impl;

import com.ryazancev.admin.dto.OrgRegRequestDTO;
import com.ryazancev.admin.dto.OrgRegRequestsResponse;
import com.ryazancev.admin.model.OrgRegRequest;
import com.ryazancev.admin.model.RequestStatus;
import com.ryazancev.admin.repository.OrgRegRequestRepository;
import com.ryazancev.admin.service.OrgRegRequestService;
import com.ryazancev.admin.util.mapper.OrgRegRequestMapper;
import com.ryazancev.validation.OnCreate;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrgRegRequestServiceImpl implements OrgRegRequestService {

    private final OrgRegRequestRepository orgRegRequestRepository;
    private final OrgRegRequestMapper orgRegRequestMapper;

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

        return orgRegRequestMapper.toDto(updated);
        //todo: handle change status on request and also in organization service with kafka
        //todo: send notification to user about admin's decision
    }

    @Transactional
    @Override
    public void create(
            @Validated(OnCreate.class)
            OrgRegRequestDTO orgRegRequestDTO) {

        OrgRegRequest toSave = orgRegRequestMapper.toEntity(orgRegRequestDTO);

        toSave.setStatus(RequestStatus.ON_REVIEW);
        toSave.setCreatedAt(LocalDateTime.now());

        orgRegRequestRepository.save(toSave);
    }
}


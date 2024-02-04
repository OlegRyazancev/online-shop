package com.ryazancev.admin.service;


import com.ryazancev.admin.dto.OrgRegRequestDTO;
import com.ryazancev.admin.dto.OrgRegRequestsResponse;
import com.ryazancev.admin.model.RequestStatus;

public interface OrgRegRequestService {

    OrgRegRequestsResponse getAll();

    OrgRegRequestDTO changeStatus(Long requestId, RequestStatus status);

    void create(OrgRegRequestDTO orgRegRequestDTO);

}

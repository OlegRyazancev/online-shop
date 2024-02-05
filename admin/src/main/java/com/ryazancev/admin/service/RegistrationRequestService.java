package com.ryazancev.admin.service;


import com.ryazancev.dto.admin.RegistrationRequestDTO;
import com.ryazancev.dto.admin.RegistrationRequestsResponse;
import com.ryazancev.dto.admin.RequestStatus;

public interface RegistrationRequestService {

    RegistrationRequestsResponse getAll();
    RegistrationRequestsResponse getProductRegistrationRequests();
    RegistrationRequestsResponse getOrganizationRegistrationRequests();
    RegistrationRequestDTO changeStatus(Long requestId, RequestStatus status);
    void create(RegistrationRequestDTO registrationRequestDTO);

}

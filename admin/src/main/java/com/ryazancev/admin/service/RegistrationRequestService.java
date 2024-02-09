package com.ryazancev.admin.service;


import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.dto.admin.RequestStatus;

import java.util.List;

public interface RegistrationRequestService {

    List<RegistrationRequest> getAll();
    List<RegistrationRequest> getProductRegistrationRequests();
    List<RegistrationRequest> getOrganizationRegistrationRequests();
    RegistrationRequest changeStatus(Long requestId, RequestStatus status);
    void create(RegistrationRequest registrationRequest);

}

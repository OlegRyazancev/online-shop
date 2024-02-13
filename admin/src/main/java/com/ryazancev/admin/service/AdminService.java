package com.ryazancev.admin.service;


import com.ryazancev.admin.dto.FreezeRequest;
import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.dto.admin.RequestStatus;

import java.util.List;

public interface AdminService {

    List<RegistrationRequest> getAll();
    List<RegistrationRequest> getProductRegistrationRequests();
    List<RegistrationRequest> getOrganizationRegistrationRequests();
    RegistrationRequest changeStatus(Long requestId, RequestStatus status);
    String freezeObject(FreezeRequest request);
    void create(RegistrationRequest registrationRequest);
}

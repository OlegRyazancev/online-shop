package com.ryazancev.admin.service;


import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.dto.admin.ObjectRequest;
import com.ryazancev.dto.admin.RequestStatus;

import java.util.List;

public interface AdminService {

    List<RegistrationRequest> getAll();

    List<RegistrationRequest> getProductRegistrationRequests();

    List<RegistrationRequest> getOrganizationRegistrationRequests();

    RegistrationRequest changeRegistrationStatus(Long requestId,
                                                 RequestStatus status);

    String changeObjectStatus(ObjectRequest request);

    void create(RegistrationRequest registrationRequest);
}

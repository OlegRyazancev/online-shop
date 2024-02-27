package com.ryazancev.admin.service;


import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.UserLockRequest;
import com.ryazancev.common.dto.admin.enums.RequestStatus;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

public interface AdminService {

    List<RegistrationRequest> getAll();

    List<RegistrationRequest> getProductRegistrationRequests();

    List<RegistrationRequest> getOrganizationRegistrationRequests();

    RegistrationRequest changeRegistrationStatus(Long requestId,
                                                 RequestStatus status);

    String changeObjectStatus(ObjectRequest request);

    String toggleUserLock(UserLockRequest request);

    void create(RegistrationRequest registrationRequest);
}

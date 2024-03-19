package com.ryazancev.admin.controller;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.service.AdminService;
import com.ryazancev.admin.util.AdminUtil;
import com.ryazancev.admin.util.mapper.AdminMapper;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.RegistrationRequestsResponse;
import com.ryazancev.common.dto.admin.UserLockRequest;
import com.ryazancev.common.dto.admin.enums.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final AdminUtil adminUtil;

    @GetMapping("/requests")
    public RegistrationRequestsResponse getAllRegistrationRequests() {

        List<RegistrationRequest> requests =
                adminService.getAll();

        return RegistrationRequestsResponse.builder()
                .requests(adminMapper.toDtoList(requests))
                .build();
    }

    @GetMapping("/requests/product")
    public RegistrationRequestsResponse getProductRegistrationRequests() {

        List<RegistrationRequest> productRequests =
                adminService.getProductRegistrationRequests();

        return RegistrationRequestsResponse.builder()
                .requests(adminMapper.toDtoList(productRequests))
                .build();
    }

    @GetMapping("/requests/organization")
    public RegistrationRequestsResponse getOrganizationRegistrationRequests() {

        List<RegistrationRequest> organizationRequests =
                adminService
                        .getOrganizationRegistrationRequests();

        return RegistrationRequestsResponse.builder()
                .requests(adminMapper
                        .toDtoList(organizationRequests))
                .build();
    }

    @PutMapping("/requests/{id}")
    public RegistrationRequestDto changeRegistrationRequestStatus(
            @PathVariable("id") final Long id,
            @RequestParam("status") final String status) {

        RequestStatus statusEnum = adminUtil.castStatus(status);
        RegistrationRequest request = adminService
                .changeRegistrationStatus(id, statusEnum);

        return adminMapper.toDto(request);
    }


    @PutMapping("/change-object-status")
    public String changeObjectStatus(
            @Validated
            @RequestBody final ObjectRequest request) {

        return adminService.changeObjectStatus(request);
    }

    @PutMapping("/toggle-user-lock")
    public String toggleUserLock(
            @Validated
            @RequestBody final UserLockRequest request) {

        return adminService.toggleUserLock(request);
    }
}

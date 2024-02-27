package com.ryazancev.admin.controller;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.service.AdminService;
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

@Slf4j
@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminService adminService;
    private final AdminMapper adminMapper;


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
            @PathVariable("id") Long id,
            @RequestParam("status") String status) {

        RegistrationRequest request = adminService
                .changeRegistrationStatus(id, RequestStatus.valueOf(status));

        return adminMapper.toDto(request);
    }

    @PutMapping("/change-object-status")
    public String changeObjectStatus(
            @Validated
            @RequestBody
            ObjectRequest request) {

        return adminService.changeObjectStatus(request);
    }

    @PutMapping("/toggle-user-lock")
    public String toggleUserLock(
            @Validated
            @RequestBody
            UserLockRequest request) {

        return adminService.toggleUserLock(request);
    }

    //todo: send notification to user

}

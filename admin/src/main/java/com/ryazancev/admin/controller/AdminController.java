package com.ryazancev.admin.controller;

import com.ryazancev.admin.dto.FreezeRequest;
import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.service.AdminService;
import com.ryazancev.admin.util.mapper.AdminMapper;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import com.ryazancev.dto.admin.RegistrationRequestsResponse;
import com.ryazancev.dto.admin.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
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
    public RegistrationRequestDTO changeRegistrationRequestStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status) {

        RegistrationRequest request = adminService
                .changeStatus(id, RequestStatus.valueOf(status));

        return adminMapper.toDto(request);
    }

    @PutMapping("/freeze")
    public String freezeObject(@RequestBody FreezeRequest request) {

        return adminService.freezeObject(request);
    }


    //todo: delete customer


    //todo: send notification to user

    //todo: freeze product
    //todo: freeze organization
    //todo: freeze user

}

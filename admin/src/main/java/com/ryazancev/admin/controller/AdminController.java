package com.ryazancev.admin.controller;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.service.RegistrationRequestService;
import com.ryazancev.admin.util.mapper.RegistrationRequestMapper;
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

    private final RegistrationRequestService registrationRequestService;
    private final RegistrationRequestMapper registrationRequestMapper;


    @GetMapping("/requests")
    public RegistrationRequestsResponse getAllRegistrationRequests() {

        List<RegistrationRequest> requests =
                registrationRequestService.getAll();

        return RegistrationRequestsResponse.builder()
                .requests(registrationRequestMapper.toDtoList(requests))
                .build();
    }

    @GetMapping("/requests/product")
    public RegistrationRequestsResponse getProductRegistrationRequests() {

        List<RegistrationRequest> productRequests =
                registrationRequestService.getProductRegistrationRequests();

        return RegistrationRequestsResponse.builder()
                .requests(registrationRequestMapper.toDtoList(productRequests))
                .build();
    }

    @GetMapping("/requests/organization")
    public RegistrationRequestsResponse getOrganizationRegistrationRequests() {

        List<RegistrationRequest> organizationRequests =
                registrationRequestService
                        .getOrganizationRegistrationRequests();

        return RegistrationRequestsResponse.builder()
                .requests(registrationRequestMapper
                        .toDtoList(organizationRequests))
                .build();
    }

    @PutMapping("/requests/{id}")
    public RegistrationRequestDTO changeRegistrationRequestStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status) {

        RegistrationRequest request = registrationRequestService
                .changeStatus(id, RequestStatus.valueOf(status));

        return registrationRequestMapper.toDto(request);
    }


    //todo: delete customer


    //todo: send notification to user

    //todo: freeze product
    //todo: freeze organization
    //todo: freeze user

}

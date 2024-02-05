package com.ryazancev.admin.controller;

import com.ryazancev.admin.service.RegistrationRequestService;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import com.ryazancev.dto.admin.RegistrationRequestsResponse;
import com.ryazancev.dto.admin.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final RegistrationRequestService registrationRequestService;


    @GetMapping("/requests")
    public RegistrationRequestsResponse getAllRegistrationRequests() {

        return registrationRequestService.getAll();
    }

    @GetMapping("/requests/product")
    public RegistrationRequestsResponse getProductRegistrationRequests() {

        return registrationRequestService
                .getProductRegistrationRequests();
    }

    @GetMapping("/requests/organization")
    public RegistrationRequestsResponse getOrganizationRegistrationRequests() {

        return registrationRequestService
                .getOrganizationRegistrationRequests();
    }

    @PutMapping("/requests/{id}")
    public RegistrationRequestDTO changeRegistrationRequestStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status) {

        return registrationRequestService
                .changeStatus(id, RequestStatus.valueOf(status));
    }




    //todo: delete product
    //todo: delete organization
    //todo: delete review
    //todo: delete customer
    //todo: delete purchase


    //todo: send notification to user

    //todo: freeze product
    //todo: freeze organization
    //todo: freeze user

}

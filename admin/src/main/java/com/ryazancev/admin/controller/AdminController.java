package com.ryazancev.admin.controller;

import com.ryazancev.admin.dto.OrgRegRequestDTO;
import com.ryazancev.admin.dto.OrgRegRequestsResponse;
import com.ryazancev.admin.model.RequestStatus;
import com.ryazancev.admin.service.OrgRegRequestService;
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

    private final OrgRegRequestService orgRegRequestService;

    @GetMapping("/requests/organizations")
    public OrgRegRequestsResponse getAllOrgRegRequests() {

        return orgRegRequestService.getAll();
    }

    @PutMapping("/requests/organizations/{id}")
    public OrgRegRequestDTO changeOrgRegRequestStatus(
            @PathVariable("id") Long id,
            @RequestParam String status) {

        return orgRegRequestService
                .changeStatus(id, RequestStatus.valueOf(status));
    }


    //todo: delete product
    //todo: delete organization
    //todo: delete review
    //todo: delete customer
//    todo: purchase

    //todo: check product registration requests


    //todo: permit request
    //todo: not permit

    //todo: send notification to user

    //todo: freeze organization
    //todo: freeze user

}

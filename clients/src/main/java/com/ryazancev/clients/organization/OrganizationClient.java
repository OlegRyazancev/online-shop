package com.ryazancev.clients.organization;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "organization")
public interface OrganizationClient {

    @GetMapping("api/v1/organizations/{organizationId}")
    OrganizationDTO getById(@PathVariable("organizationId") Long organizationId);

}

package com.ryazancev.clients;

import com.ryazancev.config.FeignClientsConfiguration;
import com.ryazancev.dto.organization.OrganizationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "organization",
        configuration = FeignClientsConfiguration.class
)
public interface OrganizationClient {

    @GetMapping("api/v1/organizations/{id}/simple")
    OrganizationDTO getSimpleById(
            @PathVariable("id") Long id);

    @GetMapping("api/v1/organizations/{id}/owner")
    Long getOwnerId(
            @PathVariable("id") Long id);

}

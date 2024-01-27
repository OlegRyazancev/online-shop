package com.ryazancev.clients.organization;

import com.ryazancev.config.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "organization",
        configuration = FeignClientsConfiguration.class
)
public interface OrganizationClient {

    @GetMapping("api/v1/organizations/{organizationId}")
    OrganizationSimpleDTO getById(@PathVariable("organizationId") Long organizationId);

}

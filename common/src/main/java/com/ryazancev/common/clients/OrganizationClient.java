package com.ryazancev.common.clients;

import com.ryazancev.common.config.FeignClientsConfiguration;
import com.ryazancev.common.dto.organization.OrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Oleg Ryazancev
 */

@FeignClient(
        name = "organization",
        configuration = FeignClientsConfiguration.class,
        url = "${clients.organization.url}"
)
public interface OrganizationClient {

    @GetMapping("api/v1/organizations/{id}/simple")
    OrganizationDto getSimpleById(
            @PathVariable("id") Long id);

    @GetMapping("api/v1/organizations/{id}/owner-id")
    Long getOwnerId(
            @PathVariable("id") Long organizationId);

}

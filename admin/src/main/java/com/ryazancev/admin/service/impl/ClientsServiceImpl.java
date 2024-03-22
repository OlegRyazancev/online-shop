package com.ryazancev.admin.service.impl;

import com.ryazancev.admin.service.ClientsService;
import com.ryazancev.admin.util.exception.CustomErrorCode;
import com.ryazancev.common.clients.OrganizationClient;
import com.ryazancev.common.clients.ProductClient;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.Fallback;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final ProductClient productClient;

    private final OrganizationClient organizationClient;

    @Override
    @CircuitBreaker(
            name = "admin",
            fallbackMethod = "getSimpleProductFallback"
    )
    public Element getSimpleProductById(final Long productId) {

        return productClient.getSimpleById(productId);
    }

    @Override
    @CircuitBreaker(
            name = "admin",
            fallbackMethod = "getSimpleOrganizationFallback"
    )
    public Element getSimpleOrganizationById(final Long organizationId) {

        return organizationClient.getSimpleById(organizationId);
    }

    @Override
    @CircuitBreaker(
            name = "admin",
            fallbackMethod = "getOwnerIdFallback"
    )
    public Long getOwnerIdByProductId(final Long productId) {

        return productClient.getOwnerId(productId);
    }

    @Override
    @CircuitBreaker(
            name = "admin",
            fallbackMethod = "getOwnerIdFallback"
    )
    public Long getOwnerIdByOrganizationId(final Long organizationId) {

        return organizationClient.getOwnerId(organizationId);
    }

//    Fallback methods

    private Element getSimpleOrganizationFallback(final Exception e) {

        return Fallback.builder()
                .message(
                        CustomErrorCode.OS_ADMIN_SERVICE_UNAVAILABLE_503
                                .getMessage(ServiceStage.ORGANIZATION)
                )
                .build();
    }

    private Element getSimpleProductFallback(final Exception e) {

        return Fallback.builder()
                .message(
                        CustomErrorCode.OS_ADMIN_SERVICE_UNAVAILABLE_503
                                .getMessage(ServiceStage.PRODUCT)
                )
                .build();
    }

    private Long getOwnerIdFallback(final Exception e) {

        return -1L;
    }
}

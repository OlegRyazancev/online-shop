package com.ryazancev.admin.service.impl;

import com.ryazancev.admin.service.ClientsService;
import com.ryazancev.common.clients.OrganizationClient;
import com.ryazancev.common.clients.ProductClient;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.Fallback;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final ProductClient productClient;

    private final OrganizationClient organizationClient;

    private final MessageSource messageSource;

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
                        messageSource.getMessage(
                                "exception.admin.service_unavailable",
                                new Object[]{ServiceStage.ORGANIZATION},
                                Locale.getDefault()
                        )
                )
                .build();
    }

    private Element getSimpleProductFallback(final Exception e) {

        return Fallback.builder()
                .message(
                        messageSource.getMessage(
                                "exception.admin.service_unavailable",
                                new Object[]{ServiceStage.PRODUCT},
                                Locale.getDefault()
                        )
                )
                .build();
    }

    private Long getOwnerIdFallback(final Exception e) {

        return -1L;
    }
}

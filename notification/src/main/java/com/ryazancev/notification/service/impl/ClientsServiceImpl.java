package com.ryazancev.notification.service.impl;

import com.ryazancev.common.clients.CustomerClient;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.Fallback;
import com.ryazancev.notification.service.ClientsService;
import com.ryazancev.notification.util.exception.CustomErrorCode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final CustomerClient customerClient;

    @Override
    @CircuitBreaker(
            name = "notification",
            fallbackMethod = "getSimpleCustomerFallback"
    )
    public Element getSimpleCustomerById(final Long customerId) {

        return customerClient.getSimpleById(customerId);
    }

    private Element getSimpleCustomerFallback(final Exception e) {

        return Fallback.builder()
                .message(
                        CustomErrorCode
                                .OS_NOTIFICATION_SERVICE_UNAVAILABLE_503
                                .getMessage(ServiceStage.CUSTOMER)
                )
                .build();
    }
}

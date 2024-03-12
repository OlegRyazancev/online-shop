package com.ryazancev.notification.service.impl;

import com.ryazancev.common.clients.CustomerClient;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.Fallback;
import com.ryazancev.notification.service.ClientsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ryazancev.notification.util.exception.Message.CUSTOMER_SERVICE_UNAVAILABLE;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final CustomerClient customerClient;

    @Override
    @CircuitBreaker(
            name = "review",
            fallbackMethod = "getSimpleCustomerFallback"
    )
    public Element getSimpleCustomerById(Long customerId) {

        return customerClient.getSimpleById(customerId);
    }

    private Element getSimpleCustomerFallback(Exception e) {

        return Fallback.builder()
                .message(CUSTOMER_SERVICE_UNAVAILABLE)
                .build();
    }
}

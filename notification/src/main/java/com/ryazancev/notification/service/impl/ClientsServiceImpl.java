package com.ryazancev.notification.service.impl;

import com.ryazancev.common.clients.CustomerClient;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.Fallback;
import com.ryazancev.notification.service.ClientsService;
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

    private final CustomerClient customerClient;
    private final MessageSource messageSource;

    @Override
    @CircuitBreaker(
            name = "notification",
            fallbackMethod = "getSimpleCustomerFallback"
    )
    public Element getSimpleCustomerById(Long customerId) {

        return customerClient.getSimpleById(customerId);
    }

    private Element getSimpleCustomerFallback(Exception e) {

        return Fallback.builder()
                .message(
                        messageSource.getMessage(
                                "customer_service_unavailable",
                                null,
                                Locale.getDefault()
                        )
                )
                .build();
    }
}

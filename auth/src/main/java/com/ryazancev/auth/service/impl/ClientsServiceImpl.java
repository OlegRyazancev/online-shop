package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.service.ClientsService;
import com.ryazancev.clients.CustomerClient;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ryazancev.auth.util.exception.Message.CUSTOMER_SERVICE_UNAVAILABLE;

/**
 * @author Oleg Ryazancev
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final CustomerClient customerClient;

    @Override
    @CircuitBreaker(
            name = "auth",
            fallbackMethod = "customerServiceUnavailable"
    )
    public Object createCustomer(CustomerDto customerDto) {

        return customerClient.createCustomer(customerDto);
    }

    private Object customerServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                CUSTOMER_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}

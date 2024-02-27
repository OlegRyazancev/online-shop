package com.ryazancev.auth.service;

import com.ryazancev.common.dto.customer.CustomerDto;

public interface ClientsService {

    Object createCustomer (CustomerDto customerDto);
}

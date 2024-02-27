package com.ryazancev.auth.service;

import com.ryazancev.common.dto.customer.CustomerDto;

/**
 * @author Oleg Ryazancev
 */

public interface ClientsService {

    Object createCustomer (CustomerDto customerDto);
}

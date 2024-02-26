package com.ryazancev.purchase.service;

import com.ryazancev.dto.Component;

public interface ClientsService {

    Component getSimpleCustomer(Long customerId);

    Component getSimpleProduct(Long productId);

    Object getCustomerBalance(Long customerId);

    Object getProductPriceAndQuantity(Long productId);

    Object getProductOwnerId(Long productId);
}

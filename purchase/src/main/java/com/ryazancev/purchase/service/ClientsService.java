package com.ryazancev.purchase.service;

import com.ryazancev.dto.Element;

public interface ClientsService {

    Element getSimpleCustomer(Long customerId);

    Element getSimpleProduct(Long productId);

    Object getCustomerBalance(Long customerId);

    Object getProductPriceAndQuantity(Long productId);

    Object getProductOwnerId(Long productId);
}

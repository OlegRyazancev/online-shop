package com.ryazancev.review.service;

import com.ryazancev.dto.Element;

public interface ClientsService {

    Element getSimpleProductById(Long productId);

    Element getSimpleCustomerById(Long customerId);

    Object getPurchaseById(String purchaseId);
}

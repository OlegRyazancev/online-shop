package com.ryazancev.customer.service;

import com.ryazancev.dto.purchase.PurchaseEditDto;

public interface ClientsService {

    Object processPurchase(PurchaseEditDto purchaseEditDto);

    Object getPurchasesByCustomerId(Long customerId);

    Object getReviewsByCustomerId(Long customerId);
}
package com.ryazancev.customer.service;

import com.ryazancev.dto.*;

public interface CustomerService {
    CustomerDTO getById(Long id);

    CustomerDTO getDetailedById(Long id);

    CustomerDTO updateBalance(Long id, Double amount);

    CustomerPurchasesResponse getPurchasesByCustomerId(Long id);

    PurchaseDTO processPurchase(PurchaseEditDTO purchaseEditDTO);

    ReviewsResponse getReviewsByCustomerId(Long id);

    CustomerDTO create(CustomerDTO customerDTO);
}

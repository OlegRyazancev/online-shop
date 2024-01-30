package com.ryazancev.customer.service;

import com.ryazancev.clients.customer.dto.CustomerDTO;
import com.ryazancev.clients.customer.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.dto.PurchaseDTO;
import com.ryazancev.clients.purchase.dto.PurchaseEditDTO;
import com.ryazancev.clients.review.dto.ReviewsResponse;

public interface CustomerService {
    CustomerDTO getById(Long id);

    CustomerDTO getDetailedById(Long id);

    CustomerDTO updateBalance(Long id, Double amount);

    CustomerPurchasesResponse getPurchasesByCustomerId(Long id);

    PurchaseDTO processPurchase(PurchaseEditDTO purchaseEditDTO);

    ReviewsResponse getReviewsByCustomerId(Long id);

    CustomerDTO create(CustomerDTO customerDTO);
}

package com.ryazancev.customer.service;

import com.ryazancev.customer.model.Customer;
import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.customer.UpdateBalanceRequest;
import com.ryazancev.dto.purchase.PurchaseDTO;
import com.ryazancev.dto.purchase.PurchaseEditDTO;
import com.ryazancev.dto.review.ReviewsResponse;

public interface CustomerService {
    Customer getById(Long id);

    Customer update(Customer customer);

    CustomerPurchasesResponse getPurchasesByCustomerId(Long id);

    PurchaseDTO processPurchase(PurchaseEditDTO purchaseEditDTO);

    ReviewsResponse getReviewsByCustomerId(Long id);

    Double getBalanceByCustomerId(Long id);

    Customer updateBalance(UpdateBalanceRequest request);

    Customer create(Customer customer);
}

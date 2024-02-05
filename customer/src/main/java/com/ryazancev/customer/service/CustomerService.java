package com.ryazancev.customer.service;

import com.ryazancev.dto.customer.CustomerDTO;
import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.customer.UpdateBalanceRequest;
import com.ryazancev.dto.purchase.PurchaseDTO;
import com.ryazancev.dto.purchase.PurchaseEditDTO;
import com.ryazancev.dto.review.ReviewsResponse;

public interface CustomerService {
    CustomerDTO getSimpleById(Long id);

    CustomerDTO getDetailedById(Long id);

    CustomerDTO create(CustomerDTO customerDTO);

    CustomerDTO update(CustomerDTO customerDTO);

    CustomerPurchasesResponse getPurchasesByCustomerId(Long id);

    PurchaseDTO processPurchase(PurchaseEditDTO purchaseEditDTO);

    ReviewsResponse getReviewsByCustomerId(Long id);

    void updateBalance(UpdateBalanceRequest request);

}

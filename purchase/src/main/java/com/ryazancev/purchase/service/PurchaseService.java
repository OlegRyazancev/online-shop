package com.ryazancev.purchase.service;

import com.ryazancev.clients.customer.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.dto.PurchaseDTO;
import com.ryazancev.clients.purchase.dto.PurchasePostDTO;

public interface PurchaseService {

    PurchaseDTO processPurchase(PurchasePostDTO purchasePostDTO);

    CustomerPurchasesResponse getByCustomerId(Long customerId);

}

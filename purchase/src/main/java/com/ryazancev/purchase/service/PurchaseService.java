package com.ryazancev.purchase.service;

import com.ryazancev.clients.customer.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.PurchaseDTO;

public interface PurchaseService {

    PurchaseDTO processPurchase(PurchaseDTO purchase);

    CustomerPurchasesResponse getByCustomerId(Long customerId);
}

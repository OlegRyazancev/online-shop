package com.ryazancev.purchase.service;

import com.ryazancev.clients.customer.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.dto.PurchaseDTO;
import com.ryazancev.clients.purchase.dto.PurchaseEditDTO;

public interface PurchaseService {

    PurchaseDTO processPurchase(PurchaseEditDTO purchaseEditDTO);

    CustomerPurchasesResponse getByCustomerId(Long customerId);

}

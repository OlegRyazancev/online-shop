package com.ryazancev.purchase.service;

import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.purchase.PurchaseDTO;
import com.ryazancev.dto.purchase.PurchaseEditDTO;

public interface PurchaseService {

    PurchaseDTO processPurchase(PurchaseEditDTO purchaseEditDTO);

    CustomerPurchasesResponse getByCustomerId(Long customerId);

}

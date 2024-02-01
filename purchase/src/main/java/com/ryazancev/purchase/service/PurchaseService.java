package com.ryazancev.purchase.service;

import com.ryazancev.dto.CustomerPurchasesResponse;
import com.ryazancev.dto.PurchaseDTO;
import com.ryazancev.dto.PurchaseEditDTO;

public interface PurchaseService {

    PurchaseDTO processPurchase(PurchaseEditDTO purchaseEditDTO);

    CustomerPurchasesResponse getByCustomerId(Long customerId);

}

package com.ryazancev.purchase.service;

import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.purchase.PurchaseDto;
import com.ryazancev.dto.purchase.PurchaseEditDto;

public interface PurchaseService {

    PurchaseDto processPurchase(PurchaseEditDto purchaseEditDto);

    CustomerPurchasesResponse getByCustomerId(Long customerId);

}

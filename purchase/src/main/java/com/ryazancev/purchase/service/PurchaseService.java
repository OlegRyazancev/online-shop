package com.ryazancev.purchase.service;

import com.ryazancev.clients.customer.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.dto.PurchaseDetailedDTO;
import com.ryazancev.clients.purchase.dto.PurchasePostDTO;

public interface PurchaseService {

    PurchaseDetailedDTO processPurchase(PurchasePostDTO purchasePostDTO);

    CustomerPurchasesResponse getByCustomerId(Long customerId);

}

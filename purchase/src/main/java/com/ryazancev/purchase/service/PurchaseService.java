package com.ryazancev.purchase.service;

import com.ryazancev.clients.customer.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.PurchaseDetailedDTO;
import com.ryazancev.clients.purchase.PurchasePostDTO;

public interface PurchaseService {

    PurchaseDetailedDTO processPurchase(PurchasePostDTO purchasePostDTO);

    CustomerPurchasesResponse getByCustomerId(Long customerId);

}

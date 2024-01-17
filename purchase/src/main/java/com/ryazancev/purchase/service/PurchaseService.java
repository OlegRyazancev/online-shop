package com.ryazancev.purchase.service;

import com.ryazancev.purchase.dto.CustomerPurchasesResponse;
import com.ryazancev.purchase.model.Purchase;

import java.util.List;

public interface PurchaseService {

    Purchase processPurchase(Purchase purchase);

    List<Purchase> getByCustomerId(Long customerId);
}

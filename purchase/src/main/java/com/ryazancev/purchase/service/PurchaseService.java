package com.ryazancev.purchase.service;

import com.ryazancev.common.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;

/**
 * @author Oleg Ryazancev
 */

public interface PurchaseService {

    PurchaseDto processPurchase(PurchaseEditDto purchaseEditDto);

    CustomerPurchasesResponse getByCustomerId(Long customerId);

    PurchaseDto getById(String id);
}

package com.ryazancev.customer.service;

import com.ryazancev.common.dto.purchase.PurchaseEditDto;

/**
 * @author Oleg Ryazancev
 */

public interface ClientsService {

    Object processPurchase(PurchaseEditDto purchaseEditDto);

    Object getPurchasesByCustomerId(Long customerId);

    Object getReviewsByCustomerId(Long customerId);
}

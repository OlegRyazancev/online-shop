package com.ryazancev.customer.service;

import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;

/**
 * @author Oleg Ryazancev
 */

public interface ClientsService {

    Object processPurchase(PurchaseEditDto purchaseEditDto);

    Object getPurchasesByCustomerId(Long customerId);

    Object getReviewsByCustomerId(Long customerId);

    Object getNotificationsByCustomerId(Long customerId, String scope);

    Object getNotificationById(String notificationId, String scope);

    Object getRecipientIdByPrivateNotificationId(String notificationId);

    Object getProductOwnerId(Long productId);

    Element getSimpleProductById(Long productId);
}

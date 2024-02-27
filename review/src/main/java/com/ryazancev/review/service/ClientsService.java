package com.ryazancev.review.service;

import com.ryazancev.common.dto.Element;

/**
 * @author Oleg Ryazancev
 */

public interface ClientsService {

    Element getSimpleProductById(Long productId);

    Element getSimpleCustomerById(Long customerId);

    Object getPurchaseById(String purchaseId);
}

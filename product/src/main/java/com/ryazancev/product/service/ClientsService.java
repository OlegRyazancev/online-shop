package com.ryazancev.product.service;

import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.review.ReviewEditDto;

/**
 * @author Oleg Ryazancev
 */

public interface ClientsService {

    Element getSimpleCustomerById(Long customerId);
    Element getSimpleOrganizationById(Long organizationId);

    Double getAverageRatingByProductId(Long id);

    Object getOrganizationOwnerIdById(Long organizationId);

    Object getReviewsByProductId(Long id);

    Object createReview(ReviewEditDto reviewEditDto);

    Object getPurchaseById(String purchaseId);
}

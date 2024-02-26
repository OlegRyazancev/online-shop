package com.ryazancev.product.service;

import com.ryazancev.dto.Element;
import com.ryazancev.dto.review.ReviewEditDto;

public interface ClientsService {

    Element getSimpleCustomerById(Long customerId);
    Element getSimpleOrganizationById(Long organizationId);

    Double getAverageRatingByProductId(Long id);

    Object getOrganizationOwnerIdById(Long organizationId);

    Object getReviewsByProductId(Long id);

    Object createReview(ReviewEditDto reviewEditDto);

    Object getPurchaseById(String purchaseId);
}

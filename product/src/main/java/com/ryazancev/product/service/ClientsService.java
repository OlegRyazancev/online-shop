package com.ryazancev.product.service;

import com.ryazancev.dto.review.ReviewEditDto;

public interface ClientsService {

    Object getSimpleOrganizationById(Long organizationId);

    Object getOrganizationOwnerIdById(Long organizationId);

    Object getAverageRatingByProductId(Long id);

    Object getReviewsByProductId(Long id);

    Object createReview(ReviewEditDto reviewEditDto);
}

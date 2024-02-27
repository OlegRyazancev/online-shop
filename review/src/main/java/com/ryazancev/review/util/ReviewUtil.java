package com.ryazancev.review.util;

import com.ryazancev.dto.review.ReviewDto;
import com.ryazancev.review.model.Review;
import com.ryazancev.review.service.ClientsService;
import com.ryazancev.review.util.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class ReviewUtil {

    private final ReviewMapper reviewMapper;
    private final ClientsService clientsService;

    public List<ReviewDto> createReviewsDtoWithCustomersInfo(
            List<Review> reviews) {

        List<ReviewDto> reviewsDto = reviewMapper.toListDto(reviews);

        for (int i = 0; i < reviews.size(); i++) {
            Long customerId = reviews.get(i).getCustomerId();
            reviewsDto.get(i)
                    .setCustomer(clientsService
                            .getSimpleCustomerById(customerId));
        }

        return reviewsDto;
    }

    public List<ReviewDto> createReviewsDtoWithProductsInfo(
            List<Review> reviews) {

        List<ReviewDto> reviewsDto = reviewMapper.toListDto(reviews);

        for (int i = 0; i < reviews.size(); i++) {
            Long productId = reviews.get(i).getProductId();
            reviewsDto.get(i)
                    .setProduct(clientsService
                            .getSimpleProductById(productId));
        }

        return reviewsDto;
    }
}

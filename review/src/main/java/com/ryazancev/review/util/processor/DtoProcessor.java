package com.ryazancev.review.util.processor;

import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.review.ReviewDto;
import com.ryazancev.common.dto.review.ReviewsResponse;
import com.ryazancev.review.model.Review;
import com.ryazancev.review.service.ClientsService;
import com.ryazancev.review.util.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class DtoProcessor {

    private final ReviewMapper reviewMapper;
    private final ClientsService clientsService;

    public ReviewDto createReviewDtoWithPurchaseDto(
            Review review, PurchaseDto purchaseDto) {

        ReviewDto reviewDto = reviewMapper.toDto(review);

        reviewDto.setPurchase(purchaseDto);

        return reviewDto;
    }

    public ReviewsResponse createReviewsResponseWithSupplier(
            List<Review> reviews, Class<?> dtoClass) {

        List<ReviewDto> reviewsDto = Collections.emptyList();

        if (!reviews.isEmpty()) {

            reviewsDto = reviewMapper.toListDto(reviews);

            if (dtoClass.equals(ProductDto.class)) {
                for (int i = 0; i < reviews.size(); i++) {
                    reviewsDto.get(i).setProduct(
                            clientsService.getSimpleProductById(
                                    reviews.get(i).getProductId()));
                }
            } else if (dtoClass.equals(CustomerDto.class)) {
                for (int i = 0; i < reviews.size(); i++) {
                    reviewsDto.get(i).setCustomer(
                            clientsService.getSimpleCustomerById(
                                    reviews.get(i).getCustomerId()));
                }
            }

        }

        return ReviewsResponse.builder()
                .reviews(reviewsDto)
                .build();
    }
}

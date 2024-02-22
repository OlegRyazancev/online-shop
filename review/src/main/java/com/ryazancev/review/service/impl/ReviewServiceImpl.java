package com.ryazancev.review.service.impl;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.product.ProductDto;
import com.ryazancev.dto.review.ReviewDto;
import com.ryazancev.dto.review.ReviewEditDto;
import com.ryazancev.dto.review.ReviewsResponse;
import com.ryazancev.review.model.Review;
import com.ryazancev.review.repository.ReviewRepository;
import com.ryazancev.review.service.ReviewService;
import com.ryazancev.review.util.exception.custom.ReviewCreationException;
import com.ryazancev.review.util.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    private final CustomerClient customerClient;
    private final ProductClient productClient;

    @Override
    public ReviewsResponse getByCustomerId(Long customerId) {

        CustomerDto existingCustomer = customerClient
                .getSimpleById(customerId);
        List<Review> reviews = reviewRepository
                .findByCustomerId(existingCustomer.getId());
        List<ReviewDto> reviewsDto = reviewMapper
                .toListDto(reviews);

        if (!reviewsDto.isEmpty()) {
            for (int i = 0; i < reviewsDto.size(); i++) {
                Long productId = reviews.get(i).getProductId();
                reviewsDto.get(i)
                        .setProduct(productClient.getSimpleById(productId));
            }
        }

        return ReviewsResponse.builder()
                .reviews(reviewsDto)
                .build();
    }

    @Override
    public ReviewsResponse getByProductId(Long productId) {

        ProductDto productDto = productClient.getSimpleById(productId);

        List<Review> reviews = reviewRepository
                .findByProductId(productDto.getId());
        List<ReviewDto> reviewsDto = reviewMapper
                .toListDto(reviews);

        if (!reviewsDto.isEmpty()) {
            for (int i = 0; i < reviewsDto.size(); i++) {
                Long customerId = reviews.get(i).getCustomerId();
                reviewsDto.get(i)
                        .setCustomer(
                                customerClient.getSimpleById(customerId)
                        );
            }
        }

        return ReviewsResponse.builder()
                .reviews(reviewsDto)
                .build();
    }

    @Override
    @Transactional
    public ReviewDto create(ReviewEditDto reviewEditDto) {

        CustomerDto selectedCustomerDto;
        ProductDto selectedProductDto;

        try {
            selectedCustomerDto = customerClient
                    .getSimpleById(reviewEditDto.getCustomerId());
            selectedProductDto = productClient
                    .getSimpleById(reviewEditDto.getProductId());
        } catch (Exception e) {
            throw new ReviewCreationException(
                    "Customer/product doesn't exists",
                    HttpStatus.NOT_FOUND);
        }

        Review toSave = reviewMapper.toEntity(reviewEditDto);
        toSave.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.insert(toSave);

        ReviewDto savedReviewDto = reviewMapper
                .toDto(saved);
        savedReviewDto.setCustomer(selectedCustomerDto);
        savedReviewDto.setProduct(selectedProductDto);

        return savedReviewDto;
    }

    @Override
    public Double getAverageRatingByProductId(Long productId) {

        List<Review> reviews = reviewRepository.findByProductId(productId);

        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    @Override
    @Transactional
    public void deleteByProductId(Long productId) {

        List<Review> reviews = reviewRepository.findByProductId(productId);

        reviewRepository.deleteAll(reviews);
        log.info("Reviews {} successfully deleted for product ID {}",
                reviews.size(), productId);
    }
}

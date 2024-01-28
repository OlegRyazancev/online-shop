package com.ryazancev.review.service.impl;

import com.ryazancev.clients.customer.CustomerClient;
import com.ryazancev.clients.customer.dto.CustomerDTO;
import com.ryazancev.clients.product.ProductClient;
import com.ryazancev.clients.product.dto.ProductSimpleDTO;
import com.ryazancev.clients.review.dto.*;
import com.ryazancev.review.model.Review;
import com.ryazancev.review.repository.ReviewRepository;
import com.ryazancev.review.service.ReviewService;
import com.ryazancev.review.util.exception.ReviewCreationException;
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
    public ReviewsCustomerResponse getByCustomerId(Long customerId) {

        CustomerDTO foundCustomer = customerClient
                .getSimpleById(customerId);
        List<Review> reviews = reviewRepository
                .findByCustomerId(foundCustomer.getId());
        List<ReviewCustomerDTO> reviewsDTO = reviewMapper
                .toCustomerDTO(reviews);

        if (!reviewsDTO.isEmpty()) {
            for (int i = 0; i < reviewsDTO.size(); i++) {
                Long productId = reviews.get(i).getProductId();
                reviewsDTO.get(i)
                        .setProduct(
                                productClient.getSimpleProductById(productId)
                        );
            }
        }

        return ReviewsCustomerResponse.builder()
                .reviews(reviewsDTO)
                .build();
    }

    @Override
    public ReviewsProductResponse getByProductId(Long productId) {

        ProductSimpleDTO foundProduct = productClient
                .getSimpleProductById(productId);
        List<Review> reviews = reviewRepository
                .findByProductId(foundProduct.getId());
        List<ReviewProductDTO> reviewsDTO = reviewMapper
                .toProductDTO(reviews);

        if (!reviewsDTO.isEmpty()) {
            for (int i = 0; i < reviewsDTO.size(); i++) {
                Long customerId = reviews.get(i).getCustomerId();
                reviewsDTO.get(i)
                        .setCustomer(
                                customerClient.getSimpleById(customerId)
                        );
            }
        }

        return ReviewsProductResponse.builder()
                .reviews(reviewsDTO)
                .build();
    }

    @Transactional
    @Override
    public ReviewDetailedDTO create(ReviewPostDTO reviewPostDTO) {

        try {
            customerClient.getSimpleById(reviewPostDTO.getCustomerId());
            productClient.getSimpleProductById(reviewPostDTO.getProductId());
        } catch (Exception e) {
            throw new ReviewCreationException(
                    "Customer/product doesn't exists",
                    HttpStatus.NOT_FOUND);
        }

        Review reviewToSave = reviewMapper.toEntity(reviewPostDTO);
        reviewToSave.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.insert(reviewToSave);

        ReviewDetailedDTO savedReviewDTO = reviewMapper
                .toDetailedDTO(savedReview);
        savedReviewDTO.setCustomer(customerClient
                .getSimpleById(reviewPostDTO.getCustomerId()));
        savedReviewDTO.setProduct(productClient
                .getSimpleProductById(reviewPostDTO.getProductId()));

        return savedReviewDTO;
    }
}

package com.ryazancev.review.service.impl;

import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.review.ReviewDto;
import com.ryazancev.common.dto.review.ReviewEditDto;
import com.ryazancev.common.dto.review.ReviewsResponse;
import com.ryazancev.review.model.Review;
import com.ryazancev.review.repository.ReviewRepository;
import com.ryazancev.review.service.ClientsService;
import com.ryazancev.review.service.ReviewService;
import com.ryazancev.review.util.ReviewUtil;
import com.ryazancev.review.util.exception.custom.ReviewCreationException;
import com.ryazancev.review.util.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.ryazancev.review.util.exception.Message.DUPLICATE_REVIEW;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewUtil reviewUtil;

    private final ClientsService clientsService;

    @Override
    public ReviewsResponse getByCustomerId(Long customerId) {

        List<Review> reviews = reviewRepository
                .findByCustomerId(customerId);

        List<ReviewDto> reviewsDto = reviews.isEmpty() ?
                Collections.emptyList()
                : reviewUtil.createReviewsDtoWithProductsInfo(reviews);

        return ReviewsResponse.builder()
                .reviews(reviewsDto)
                .build();
    }

    @Override
    public ReviewsResponse getByProductId(Long productId) {

        List<Review> reviews = reviewRepository
                .findByProductId(productId);

        List<ReviewDto> reviewsDto = reviews.isEmpty() ?
                Collections.emptyList()
                : reviewUtil.createReviewsDtoWithCustomersInfo(reviews);

        return ReviewsResponse.builder()
                .reviews(reviewsDto)
                .build();
    }

    @Override
    @Transactional
    public ReviewDto create(ReviewEditDto reviewEditDto) {

        String purchaseId = reviewEditDto.getPurchaseId();

        if (reviewRepository.findByPurchaseId(purchaseId).isPresent()) {
            throw new ReviewCreationException(
                    DUPLICATE_REVIEW,
                    HttpStatus.BAD_REQUEST);
        }

        PurchaseDto purchaseDto = (PurchaseDto) clientsService
                .getPurchaseById(purchaseId);

        CustomerDto customerDto = purchaseDto.getCustomer()
                .safelyCast(CustomerDto.class);
        ProductDto productDto = purchaseDto.getProduct()
                .safelyCast(ProductDto.class);


        Review toSave = reviewMapper.toEntity(reviewEditDto);

        toSave.setCustomerId(customerDto.getId());
        toSave.setProductId(productDto.getId());
        toSave.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.insert(toSave);

        ReviewDto savedDto = reviewMapper.toDto(saved);

        savedDto.setPurchase(purchaseDto);

        return savedDto;
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
    }
}

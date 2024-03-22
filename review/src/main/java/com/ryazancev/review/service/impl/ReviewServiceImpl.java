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
import com.ryazancev.review.util.exception.CustomExceptionFactory;
import com.ryazancev.review.util.mapper.ReviewMapper;
import com.ryazancev.review.util.processor.DtoProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    private final DtoProcessor dtoProcessor;
    private final ClientsService clientsService;

    private final MessageSource messageSource;

    @Override
    public ReviewsResponse getByCustomerId(final Long customerId) {

        List<Review> reviews = reviewRepository
                .findByCustomerId(customerId);

        return dtoProcessor
                .createReviewsResponseWithSupplier(reviews, ProductDto.class);
    }

    @Override
    public ReviewsResponse getByProductId(final Long productId) {

        List<Review> reviews = reviewRepository
                .findByProductId(productId);


        return dtoProcessor
                .createReviewsResponseWithSupplier(reviews, CustomerDto.class);
    }

    @Override
    public ReviewDto create(final ReviewEditDto reviewEditDto) {

        String purchaseId = reviewEditDto.getPurchaseId();

        if (reviewRepository.findByPurchaseId(purchaseId).isPresent()) {

            throw CustomExceptionFactory
                    .getReviewCreation()
                    .duplicate(
                            reviewEditDto.getPurchaseId()
                    );
        }

        PurchaseDto purchaseDto = (PurchaseDto) clientsService
                .getPurchaseById(purchaseId);

        CustomerDto customerDto = purchaseDto.getCustomer()
                .safelyCast(CustomerDto.class, true);
        ProductDto productDto = purchaseDto.getProduct()
                .safelyCast(ProductDto.class, true);

        Review toSave = reviewMapper.toEntity(reviewEditDto);

        toSave.setCustomerId(customerDto.getId());
        toSave.setProductId(productDto.getId());
        toSave.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.insert(toSave);

        return dtoProcessor.createReviewDtoWithPurchaseDto(saved, purchaseDto);
    }

    @Override
    public Double getAverageRatingByProductId(final Long productId) {

        List<Review> reviews = reviewRepository.findByProductId(productId);

        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    @Override
    public String deleteByProductId(final Long productId) {

        List<Review> reviews = reviewRepository.findByProductId(productId);

        reviewRepository.deleteAll(reviews);

        return messageSource.getMessage(
                "service.review.deleted",
                new Object[]{reviews.size()},
                Locale.getDefault()
        );
    }
}

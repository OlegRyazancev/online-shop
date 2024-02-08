package com.ryazancev.review.service.impl;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.clients.params.DetailedType;
import com.ryazancev.clients.params.ReviewsType;
import com.ryazancev.dto.customer.CustomerDTO;
import com.ryazancev.dto.review.ReviewDTO;
import com.ryazancev.dto.review.ReviewPostDTO;
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

        System.out.println(DetailedType.SIMPLE.getType());

        CustomerDTO foundCustomer = customerClient
                .getSimpleById(customerId);
        List<Review> reviews = reviewRepository
                .findByCustomerId(foundCustomer.getId());
        List<ReviewDTO> reviewsDTO = reviewMapper
                .toListDTO(reviews);

        if (!reviewsDTO.isEmpty()) {
            for (int i = 0; i < reviewsDTO.size(); i++) {
                Long productId = reviews.get(i).getProductId();
                reviewsDTO.get(i)
                        .setProduct(
                                productClient.getById(
                                        productId,
                                        DetailedType.SIMPLE.getType(),
                                        ReviewsType.NO_REVIEWS.getType())
                        );
            }
        }

        return ReviewsResponse.builder()
                .reviews(reviewsDTO)
                .build();
    }

    @Override
    public ReviewsResponse getByProductId(Long productId) {


        List<Review> reviews = reviewRepository
                .findByProductId(productId);
        List<ReviewDTO> reviewsDTO = reviewMapper
                .toListDTO(reviews);

        if (!reviewsDTO.isEmpty()) {
            for (int i = 0; i < reviewsDTO.size(); i++) {
                Long customerId = reviews.get(i).getCustomerId();
                reviewsDTO.get(i)
                        .setCustomer(
                                customerClient.getSimpleById(customerId)
                        );
            }
        }

        return ReviewsResponse.builder()
                .reviews(reviewsDTO)
                .build();
    }

    @Transactional
    @Override
    public ReviewDTO create(ReviewPostDTO reviewPostDTO) {

        try {
            customerClient.getSimpleById(reviewPostDTO.getCustomerId());
            productClient.getById(
                    reviewPostDTO.getProductId(),
                    DetailedType.SIMPLE.getType(),
                    ReviewsType.NO_REVIEWS.getType());
        } catch (Exception e) {
            throw new ReviewCreationException(
                    "Customer/product doesn't exists",
                    HttpStatus.NOT_FOUND);
        }

        Review toSave = reviewMapper.toEntity(reviewPostDTO);
        toSave.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.insert(toSave);

        ReviewDTO savedReviewDTO = reviewMapper
                .toDTO(saved);
        savedReviewDTO.setCustomer(customerClient
                .getSimpleById(reviewPostDTO.getCustomerId()));
        savedReviewDTO.setProduct(productClient
                .getById(reviewPostDTO.getProductId(),
                        DetailedType.SIMPLE.getType(),
                        ReviewsType.NO_REVIEWS.getType()));

        return savedReviewDTO;
    }
}

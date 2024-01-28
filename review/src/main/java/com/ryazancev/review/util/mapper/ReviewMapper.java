package com.ryazancev.review.util.mapper;

import com.ryazancev.clients.review.dto.ReviewCustomerDTO;
import com.ryazancev.clients.review.dto.ReviewDetailedDTO;
import com.ryazancev.clients.review.dto.ReviewPostDTO;
import com.ryazancev.clients.review.dto.ReviewProductDTO;
import com.ryazancev.review.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ReviewMapper {
    List<ReviewProductDTO> toProductDTO(List<Review> reviews);

    List<ReviewCustomerDTO> toCustomerDTO(List<Review> reviews);

    Review toEntity(ReviewPostDTO reviewPostDTO);

    ReviewDetailedDTO toDetailedDTO(Review savedReview);
}

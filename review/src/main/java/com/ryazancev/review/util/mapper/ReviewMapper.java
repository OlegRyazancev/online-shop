package com.ryazancev.review.util.mapper;

import com.ryazancev.dto.review.ReviewDto;
import com.ryazancev.dto.review.ReviewPostDto;
import com.ryazancev.review.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ReviewMapper {

    List<ReviewDto> toListDto(List<Review> reviews);

    Review toEntity(ReviewPostDto reviewPostDto);

    ReviewDto toDto(Review savedReview);
}

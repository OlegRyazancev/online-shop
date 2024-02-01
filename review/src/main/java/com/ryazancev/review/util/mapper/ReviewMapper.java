package com.ryazancev.review.util.mapper;

import com.ryazancev.dto.ReviewDTO;
import com.ryazancev.dto.ReviewPostDTO;
import com.ryazancev.review.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ReviewMapper {

    List<ReviewDTO> toListDTO(List<Review> reviews);

    Review toEntity(ReviewPostDTO reviewPostDTO);

    ReviewDTO toDTO(Review savedReview);
}

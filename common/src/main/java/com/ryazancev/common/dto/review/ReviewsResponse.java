package com.ryazancev.common.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response model for a list of reviews")
public class ReviewsResponse {

    @Schema(description = "List of reviews")
    private List<ReviewDto> reviews;
}

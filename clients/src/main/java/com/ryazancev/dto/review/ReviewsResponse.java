package com.ryazancev.dto.review;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewsResponse {

    private List<ReviewDto> reviews;
}

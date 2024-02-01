package com.ryazancev.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewsResponse {

    private List<ReviewDTO> reviews;
}

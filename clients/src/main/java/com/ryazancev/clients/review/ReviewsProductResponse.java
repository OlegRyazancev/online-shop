package com.ryazancev.clients.review;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewsProductResponse {

    private List<ReviewProductDTO> reviews;
}

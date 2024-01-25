package com.ryazancev.clients.review;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewsCustomerResponse {

    private List<ReviewCustomerDTO> reviews;
}

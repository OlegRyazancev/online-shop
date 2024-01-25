package com.ryazancev.clients.review;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewPostDTO {

    private String body;

    private Long productId;

    private Long customerId;

    private Integer rating;
}

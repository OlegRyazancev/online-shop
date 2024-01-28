package com.ryazancev.clients.review.dto;

import com.ryazancev.clients.product.dto.ProductSimpleDTO;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCustomerDTO {

    private String id;

    private String body;

    private ProductSimpleDTO product;

    private Integer rating;

    private LocalDateTime createdAt;

}

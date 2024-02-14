package com.ryazancev.dto.review;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.product.ProductDto;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDto {

    private String id;

    private String body;

    private ProductDto product;

    private CustomerDto customer;

    private Integer rating;

    private LocalDateTime createdAt;
}

package com.ryazancev.dto.review;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.dto.customer.CustomerDTO;
import com.ryazancev.dto.product.ProductDTO;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO {

    private String id;

    private String body;

    private ProductDTO product;

    private CustomerDTO customer;

    private Integer rating;

    private LocalDateTime createdAt;
}

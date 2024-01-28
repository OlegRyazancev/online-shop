package com.ryazancev.clients.review.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.clients.customer.dto.CustomerDTO;
import com.ryazancev.clients.product.dto.ProductDTO;
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

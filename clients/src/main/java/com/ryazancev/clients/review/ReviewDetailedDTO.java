package com.ryazancev.clients.review;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.product.ProductSimpleDTO;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDetailedDTO {

    private String id;

    private String body;

    private ProductSimpleDTO product;

    private CustomerDTO customer;

    private Integer rating;

    private LocalDateTime createdAt;
}

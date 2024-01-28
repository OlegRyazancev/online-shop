package com.ryazancev.clients.review.dto;

import com.ryazancev.clients.customer.dto.CustomerDTO;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewProductDTO {

    private String id;

    private String body;

    private CustomerDTO customer;

    private Integer rating;

    private LocalDateTime createdAt;
}

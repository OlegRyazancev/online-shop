package com.ryazancev.dto.purchase;

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
public class PurchaseDto {

    private String id;
    private CustomerDto customer;
    private ProductDto product;
    private Double amount;
    private LocalDateTime purchaseDate;
}

package com.ryazancev.dto.purchase;

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
public class PurchaseDTO {

    private String id;
    private CustomerDTO customer;
    private ProductDTO product;
    private Double amount;
    private LocalDateTime purchaseDate;
}

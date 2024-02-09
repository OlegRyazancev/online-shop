package com.ryazancev.dto.product;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceQuantityResponse {

    private Double price;
    private Integer quantityInStock;
}

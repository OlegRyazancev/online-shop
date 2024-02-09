package com.ryazancev.dto.product;


import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceQuantityResponse implements Serializable {

    private Double price;
    private Integer quantityInStock;
}

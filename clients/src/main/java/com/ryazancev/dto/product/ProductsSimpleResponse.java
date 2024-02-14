package com.ryazancev.dto.product;


import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsSimpleResponse {

    private List<ProductDto> products;
}

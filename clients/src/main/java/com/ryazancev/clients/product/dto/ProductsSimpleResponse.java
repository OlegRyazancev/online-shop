package com.ryazancev.clients.product.dto;


import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsSimpleResponse {

    private List<ProductDTO> products;
}

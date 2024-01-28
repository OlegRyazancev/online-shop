package com.ryazancev.clients.product.dto;


import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsSimpleListResponse {

    private List<ProductDTO> products;
}

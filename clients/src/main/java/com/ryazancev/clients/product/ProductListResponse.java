package com.ryazancev.clients.product;


import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListResponse {

    private List<ProductDTO> products;
}

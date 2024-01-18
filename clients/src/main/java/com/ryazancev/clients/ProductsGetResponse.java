package com.ryazancev.clients;


import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsGetResponse {

    private List<ProductDTO> products;
}

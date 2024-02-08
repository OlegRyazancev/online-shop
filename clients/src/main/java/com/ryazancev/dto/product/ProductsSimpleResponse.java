package com.ryazancev.dto.product;


import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsSimpleResponse implements Serializable {

    private List<ProductDTO> products;
}

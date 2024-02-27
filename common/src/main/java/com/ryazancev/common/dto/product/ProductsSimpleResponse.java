package com.ryazancev.common.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response model for a list of products")
public class ProductsSimpleResponse {

    @Schema(description = "List of products")
    private List<ProductDto> products;
}

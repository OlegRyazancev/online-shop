package com.ryazancev.common.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Request model of update product's quantity in stock")
public class UpdateQuantityRequest {

    @Schema(
            description = "Product ID",
            example = "1"
    )
    private Long productId;

    @Schema(
            description = "New quantity in stock",
            example = "1"
    )
    private Integer quantityInStock;
}

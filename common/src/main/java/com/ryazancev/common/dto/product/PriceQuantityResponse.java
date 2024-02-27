package com.ryazancev.common.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author Oleg Ryazancev
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response model of actual price and quantity" +
        " in stock of some product")
public class PriceQuantityResponse {

    @Schema(
            description = "Price of the product",
            example = "123.21"
    )
    private Double price;

    @Schema(
            description = "Quantity in stock of the product",
            example = "123"
    )
    private Integer quantityInStock;
}

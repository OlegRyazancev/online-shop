package com.ryazancev.common.dto.purchase;

import com.ryazancev.common.validation.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Oleg Ryazancev
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Purchase Edit DTO. Used in POST, PUT methods")
public class PurchaseEditDto {

    @Schema(
            description = "Customer ID who made the purchase",
            example = "1"
    )
    @NotNull(
            message = "Customer ID must be not null",
            groups = OnCreate.class
    )
    private Long customerId;

    @Schema(
            description = "ID of the purchased product",
            example = "1"
    )
    @NotNull(
            message = "Product ID must be not null",
            groups = OnCreate.class
    )
    private Long productId;
}

package com.ryazancev.dto.purchase;

import com.ryazancev.validation.OnCreate;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseEditDto {

    @NotNull(message = "Customer ID must be not null",
            groups = OnCreate.class)
    private Long customerId;

    @NotNull(message = "Product ID must be not null",
            groups = OnCreate.class)
    private Long productId;
}
